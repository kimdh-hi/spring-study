package com.toy.springstomp

import com.toy.springstomp.domain.chat.repository.ChatMessageRepository
import com.toy.springstomp.domain.chat.repository.RoomUserRepository
import com.toy.springstomp.infra.stomp.SessionRegistry
import com.toy.springstomp.infra.stomp.constants.Destinations
import com.toy.springstomp.infra.stomp.dto.ChatSendRequest
import com.toy.springstomp.infra.stomp.dto.MessageFrame
import com.toy.springstomp.infra.stomp.error.ErrorCode
import com.toy.springstomp.infra.stomp.error.ErrorResponse
import com.toy.springstomp.support.RecordingFrameHandler
import com.toy.springstomp.support.RecordingStompSessionHandler
import com.toy.springstomp.support.RedisTestContainer
import com.toy.springstomp.support.SubscriptionRecorder
import com.toy.springstomp.support.SubscriptionRecorderConfig
import com.toy.springstomp.ui.api.constants.X_USER_ID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.core.ParameterizedTypeReference
import org.springframework.messaging.converter.JacksonJsonMessageConverter
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.web.client.RestClient
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import tools.jackson.databind.json.JsonMapper
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

private val AWAIT_TIMEOUT = Duration.ofSeconds(5)
private const val AWAIT_INTERVAL_MILLIS = 20L
private const val CONNECT_TIMEOUT_SECONDS = 5L
private const val APP_PREFIX = "/app"

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SubscriptionRecorderConfig::class)
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

  @Autowired
  private lateinit var subscriptionRecorder: SubscriptionRecorder

  private lateinit var restClient: RestClient
  private lateinit var stompClient: WebSocketStompClient
  private lateinit var clientScheduler: ThreadPoolTaskScheduler
  private val openSessions = mutableListOf<StompSession>()

  @BeforeEach
  fun setUp() {
    restClient = RestClient.create("http://localhost:$port")
    clientScheduler = ThreadPoolTaskScheduler().apply {
      poolSize = 1
      setThreadNamePrefix("stomp-client-")
      afterPropertiesSet()
    }
    stompClient = WebSocketStompClient(StandardWebSocketClient()).apply {
      messageConverter = JacksonJsonMessageConverter(jsonMapper)
      taskScheduler = clientScheduler
    }
    roomUserRepository.deleteAll()
    messageRepository.deleteAll()
    subscriptionRecorder.clear()
  }

  @AfterEach
  fun tearDown() {
    openSessions.forEach { runCatching { it.disconnect() } }
    openSessions.clear()
    clientScheduler.destroy()
  }

  @Test
  fun `a subscribed room user receives messages`() {
    joinRoom("room-1", "user-1")
    joinRoom("room-1", "user-2")
    val sender = connect("user-1", "device-1")
    val roomUser = connect("user-2", "device-2")
    subscribeRoom(sender, "room-1")
    val received = subscribeRoom(roomUser, "room-1")

    send(sender, "room-1", "hello")

    val message = assertNotNull(received.awaitFrame())
    assertEquals("hello", message.text)
    assertEquals("user-1", message.senderUserId)
  }

  @Test
  fun `subscribing to a room the user did not join is rejected`() {
    joinRoom("room-1", "user-2")
    val outsider = connect("user-1", "device-1")

    outsider.session.subscribe(Destinations.roomTopic("room-1"), RecordingFrameHandler(MessageFrame::class.java))

    val error = assertNotNull(outsider.handler.awaitError())
    assertEquals(ErrorCode.NOT_A_ROOM_USER.message, error.message)
    assertEquals(ErrorResponse.of(ErrorCode.NOT_A_ROOM_USER), error.response)
  }

  @Test
  fun `a room joined after connecting can be subscribed`() {
    joinRoom("room-1", "user-2")
    val newRoomUser = connect("user-1", "device-1")
    val sender = connect("user-2", "device-2")

    joinRoom("room-1", "user-1")

    val received = subscribeRoom(newRoomUser, "room-1")
    subscribeRoom(sender, "room-1")
    send(sender, "room-1", "welcome")

    assertNotNull(received.awaitFrame())
  }

  @Test
  fun `a session stops receiving once it unsubscribes`() {
    joinRoom("room-1", "user-1")
    joinRoom("room-1", "user-2")
    val leaving = connect("user-1", "device-1")
    val sender = connect("user-2", "device-2")
    val received = RecordingFrameHandler(MessageFrame::class.java)
    val subscription = leaving.session.subscribe(Destinations.roomTopic("room-1"), received)
    awaitSubscribed("user-1", "room-1")
    subscribeRoom(sender, "room-1")

    leaveRoom("room-1", "user-1")
    subscription.unsubscribe()
    awaitUnsubscribed("user-1", "room-1")
    send(sender, "room-1", "after leave")

    assertTrue(received.hasNoFrame())
  }

  @Test
  fun `a second connection from the same device closes the previous session`() {
    joinRoom("room-1", "user-1")
    val previous = connect("user-1", "device-1")

    connect("user-1", "device-1")

    assertTrue(previous.handler.awaitDisconnect())
  }

  @Test
  fun `sending to a room the user did not join is rejected`() {
    joinRoom("room-1", "user-2")
    val outsider = connect("user-1", "device-1")
    val roomUser = connect("user-2", "device-2")
    val received = subscribeRoom(roomUser, "room-1")

    send(outsider, "room-1", "intruder")

    assertTrue(received.hasNoFrame())
    assertEquals(0, messageRepository.count())
  }

  @Test
  fun `sending to a room the user left is rejected`() {
    joinRoom("room-1", "user-1")
    joinRoom("room-1", "user-2")
    val leaving = connect("user-1", "device-1")
    val roomUser = connect("user-2", "device-2")
    subscribeRoom(leaving, "room-1")
    val received = subscribeRoom(roomUser, "room-1")

    leaveRoom("room-1", "user-1")
    send(leaving, "room-1", "after leave")

    assertTrue(received.hasNoFrame())
    assertEquals(0, messageRepository.count())
  }

  @Test
  fun `backfill returns messages after the given id`() {
    joinRoom("room-1", "user-1")
    joinRoom("room-1", "user-2")
    val sender = connect("user-1", "device-1")
    val roomUser = connect("user-2", "device-2")
    subscribeRoom(sender, "room-1")
    val received = subscribeRoom(roomUser, "room-1")

    send(sender, "room-1", "first")
    val first = assertNotNull(received.awaitFrame())
    send(sender, "room-1", "second")
    assertNotNull(received.awaitFrame())

    val backfilled = restClient.get()
      .uri("/api/rooms/room-1/messages?afterId={afterId}", first.id)
      .header(X_USER_ID, "user-2")
      .retrieve()
      .body(object : ParameterizedTypeReference<List<MessageFrame>>() {})

    assertEquals(listOf("second"), backfilled?.map { it.text })
  }

  private fun joinRoom(roomId: String, userId: String) {
    restClient.put().uri("/api/rooms/$roomId/me").header(X_USER_ID, userId).retrieve().toBodilessEntity()
  }

  private fun leaveRoom(roomId: String, userId: String) {
    restClient.delete().uri("/api/rooms/$roomId/me").header(X_USER_ID, userId).retrieve().toBodilessEntity()
  }

  private fun send(connection: Connection, roomId: String, text: String) {
    connection.session.send("$APP_PREFIX/rooms/$roomId", ChatSendRequest(text))
  }

  private fun connect(userId: String, deviceId: String): Connection {
    val handler = RecordingStompSessionHandler()
    val session = stompClient
      .connectAsync("ws://localhost:$port/ws/chat?userId=$userId&deviceId=$deviceId", handler)
      .get(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
    openSessions.add(session)
    awaitRegistered(deviceId)

    return Connection(userId, session, handler)
  }

  private fun subscribeRoom(connection: Connection, roomId: String): RecordingFrameHandler<MessageFrame> {
    val frameHandler = RecordingFrameHandler(MessageFrame::class.java)
    connection.session.subscribe(Destinations.roomTopic(roomId), frameHandler)
    awaitSubscribed(connection.userId, roomId)

    return frameHandler
  }

  private fun awaitRegistered(deviceId: String) =
    await("session of $deviceId is registered") { sessionRegistry.findEntryByDeviceId(deviceId) != null }

  private fun awaitSubscribed(userId: String, roomId: String) =
    await("session of $userId subscribes $roomId") {
      subscriptionRecorder.isSubscribed(userId, Destinations.roomTopic(roomId))
    }

  private fun awaitUnsubscribed(userId: String, roomId: String) =
    await("session of $userId unsubscribes $roomId") {
      !subscriptionRecorder.isSubscribed(userId, Destinations.roomTopic(roomId))
    }

  private fun await(description: String, condition: () -> Boolean) {
    val deadline = System.nanoTime() + AWAIT_TIMEOUT.toNanos()
    while (System.nanoTime() < deadline) {
      if (condition()) return
      Thread.sleep(AWAIT_INTERVAL_MILLIS)
    }
    fail("timed out waiting until $description")
  }

  private data class Connection(
    val userId: String,
    val session: StompSession,
    val handler: RecordingStompSessionHandler,
  )
}
