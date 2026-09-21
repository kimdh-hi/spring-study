package com.toy.springrawwebsocket.chat

import com.toy.springrawwebsocket.domain.chat.repository.ChatMessageRepository
import com.toy.springrawwebsocket.domain.chat.repository.RoomUserRepository
import com.toy.springrawwebsocket.infra.ws.SessionEntry
import com.toy.springrawwebsocket.infra.ws.SessionRegistry
import com.toy.springrawwebsocket.support.RecordingWebSocketHandler
import com.toy.springrawwebsocket.support.RedisTestContainer
import com.toy.springrawwebsocket.ui.api.constants.X_USER_ID
import com.toy.springrawwebsocket.ui.api.dto.ChatMessageResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.client.RestClient
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import tools.jackson.databind.json.JsonMapper
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

private val AWAIT_TIMEOUT = Duration.ofSeconds(5)
private const val AWAIT_INTERVAL_MILLIS = 20L

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatFlowIntegrationTest : RedisTestContainer() {
  @LocalServerPort
  private var port: Int = 0

  @Autowired
  private lateinit var jsonMapper: JsonMapper

  @Autowired
  private lateinit var roomUserRepository: RoomUserRepository

  @Autowired
  private lateinit var messageRepository: ChatMessageRepository

  @Autowired
  private lateinit var sessionRegistry: SessionRegistry

  private lateinit var restClient: RestClient
  private val openSessions = mutableListOf<WebSocketSession>()

  @BeforeEach
  fun setUp() {
    restClient = RestClient.create("http://localhost:$port")
    roomUserRepository.deleteAll()
    messageRepository.deleteAll()
  }

  @AfterEach
  fun tearDown() {
    openSessions.forEach { runCatching { it.close(CloseStatus.NORMAL) } }
    openSessions.clear()
  }

  @Test
  fun `a room user receives messages while an outsider does not`() {
    joinRoom("room-1", "user-1")
    joinRoom("room-1", "user-2")
    val sender = connect("user-1", "device-1")
    val roomUser = connect("user-2", "device-2")
    val outsider = connect("user-3", "device-3")

    send(sender, "room-1", "hello")

    val received = roomUser.handler.awaitFrame("chat.message")
    assertNotNull(received)
    assertEquals("hello", received.path("text").asString())
    assertEquals("user-1", received.path("senderUserId").asString())
    assertTrue(outsider.handler.hasNoFrame())
  }

  @Test
  fun `connecting subscribes to every persisted room`() {
    joinRoom("room-1", "user-1")
    joinRoom("room-2", "user-1")

    connect("user-1", "device-1")

    awaitSubscribed("user-1", "room-1", "room-2")
    assertEquals(setOf("room-1", "room-2"), sessionEntryOf("user-1").subscribedRooms())
  }

  @Test
  fun `joining a room over rest subscribes the already connected session`() {
    joinRoom("room-1", "user-2")
    val newRoomUser = connect("user-1", "device-1")

    joinRoom("room-1", "user-1")

    awaitSubscribed("user-1", "room-1")
    val sender = connect("user-2", "device-2")
    send(sender, "room-1", "welcome")

    assertNotNull(newRoomUser.handler.awaitFrame("chat.message"))
  }

  @Test
  fun `leaving a room over rest unsubscribes the connected session`() {
    joinRoom("room-1", "user-1")
    joinRoom("room-1", "user-2")
    val leaving = connect("user-1", "device-1")
    val sender = connect("user-2", "device-2")
    awaitSubscribed("user-1", "room-1")

    leaveRoom("room-1", "user-1")

    awaitUnsubscribed("user-1", "room-1")
    send(sender, "room-1", "after leave")

    assertTrue(leaving.handler.hasNoFrame())
  }

  @Test
  fun `a second connection from the same device closes the previous session`() {
    joinRoom("room-1", "user-1")
    val previous = connect("user-1", "device-1")

    connect("user-1", "device-1")

    assertTrue(previous.handler.awaitClose())
    assertEquals(CloseStatus.POLICY_VIOLATION.code, previous.handler.closeStatus?.code)
  }

  @Test
  fun `sending to a room the session does not subscribe is rejected`() {
    joinRoom("room-1", "user-2")
    val outsider = connect("user-1", "device-1")
    val roomUser = connect("user-2", "device-2")

    send(outsider, "room-1", "intruder")

    assertTrue(roomUser.handler.hasNoFrame())
    assertEquals(0, messageRepository.count())
  }

  @Test
  fun `backfill returns messages after the given id`() {
    joinRoom("room-1", "user-1")
    joinRoom("room-1", "user-2")
    val sender = connect("user-1", "device-1")
    val roomUser = connect("user-2", "device-2")

    send(sender, "room-1", "first")
    val first = assertNotNull(roomUser.handler.awaitFrame("chat.message"))
    send(sender, "room-1", "second")
    assertNotNull(roomUser.handler.awaitFrame("chat.message"))

    val backfilled = restClient.get()
      .uri("/api/rooms/room-1/messages?afterId={afterId}", first.path("id").asLong())
      .header(X_USER_ID, "user-2")
      .retrieve()
      .body(object : ParameterizedTypeReference<List<ChatMessageResponse>>() {})

    assertEquals(listOf("second"), backfilled?.map { it.text })
  }

  private fun joinRoom(roomId: String, userId: String) {
    restClient.put().uri("/api/rooms/$roomId/me").header(X_USER_ID, userId).retrieve().toBodilessEntity()
  }

  private fun leaveRoom(roomId: String, userId: String) {
    restClient.delete().uri("/api/rooms/$roomId/me").header(X_USER_ID, userId).retrieve().toBodilessEntity()
  }

  private fun send(connection: Connection, roomId: String, text: String) {
    val frame = jsonMapper.writeValueAsString(mapOf("type" to "chat.send", "roomId" to roomId, "text" to text))
    connection.session.sendMessage(TextMessage(frame))
  }

  private fun connect(userId: String, deviceId: String): Connection {
    val handler = RecordingWebSocketHandler(jsonMapper)
    val session = StandardWebSocketClient()
      .execute(handler, "ws://localhost:$port/ws/chat?userId=$userId&deviceId=$deviceId")
      .get(5, TimeUnit.SECONDS)
    openSessions.add(session)
    awaitRegistered(userId)

    return Connection(session, handler)
  }

  private fun awaitRegistered(userId: String) =
    await("session of $userId is registered") { sessionRegistry.findEntriesByUserId(userId).isNotEmpty() }

  private fun awaitUnsubscribed(userId: String, roomId: String) =
    await("session of $userId unsubscribes $roomId") {
      sessionRegistry.findEntriesByUserId(userId).none { it.isSubscribed(roomId) }
    }

  private fun awaitSubscribed(userId: String, vararg roomIds: String) =
    await("session of $userId subscribes ${roomIds.toList()}") {
      sessionRegistry.findEntriesByUserId(userId).any { entry -> roomIds.all { entry.isSubscribed(it) } }
    }

  private fun await(description: String, condition: () -> Boolean) {
    val deadline = System.nanoTime() + AWAIT_TIMEOUT.toNanos()
    while (System.nanoTime() < deadline) {
      if (condition()) return
      Thread.sleep(AWAIT_INTERVAL_MILLIS)
    }
    fail("timed out waiting until $description")
  }

  private fun sessionEntryOf(userId: String): SessionEntry = sessionRegistry.findEntriesByUserId(userId).single()

  private data class Connection(
    val session: WebSocketSession,
    val handler: RecordingWebSocketHandler,
  )
}
