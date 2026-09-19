package com.toy.springrawwebsocket.infra.ws

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.web.socket.WebSocketSession
import java.time.Duration
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class SessionRegistryTest {
  private val registry = SessionRegistry()

  @Test
  fun `first local subscriber of a room is reported so the room channel can be opened`() {
    val entry = entry(sessionId = "session-1", deviceId = "device-1", userId = "user-1")
    registry.register(entry)

    assertTrue(registry.subscribe(entry, "room-1"))
    assertFalse(registry.subscribe(entry, "room-1"))
  }

  @Test
  fun `room channel stays open while another local session still subscribes`() {
    val first = entry(sessionId = "session-1", deviceId = "device-1", userId = "user-1")
    val second = entry(sessionId = "session-2", deviceId = "device-2", userId = "user-2")
    registry.register(first)
    registry.register(second)
    registry.subscribe(first, "room-1")
    registry.subscribe(second, "room-1")

    assertFalse(registry.unsubscribe(first, "room-1"))
    assertTrue(registry.unsubscribe(second, "room-1"))
  }

  @Test
  fun `room lookup returns every locally subscribed session`() {
    val first = entry(sessionId = "session-1", deviceId = "device-1", userId = "user-1")
    val second = entry(sessionId = "session-2", deviceId = "device-2", userId = "user-2")
    registry.register(first)
    registry.register(second)
    registry.subscribe(first, "room-1")
    registry.subscribe(second, "room-1")
    registry.subscribe(second, "room-2")

    assertEquals(setOf("session-1", "session-2"), registry.findEntriesByRoomId("room-1").map { it.sessionId }.toSet())
    assertEquals(listOf("session-2"), registry.findEntriesByRoomId("room-2").map { it.sessionId })
  }

  @Test
  fun `registering the same device reports the replaced session`() {
    val previous = entry(sessionId = "session-1", deviceId = "device-1", userId = "user-1")
    val current = entry(sessionId = "session-2", deviceId = "device-1", userId = "user-1")
    registry.register(previous)

    assertSame(previous, registry.register(current))
    assertSame(current, registry.findEntryByDeviceId("device-1"))
  }

  @Test
  fun `removing a replaced session keeps the current device mapping`() {
    val previous = entry(sessionId = "session-1", deviceId = "device-1", userId = "user-1")
    val current = entry(sessionId = "session-2", deviceId = "device-1", userId = "user-1")
    registry.register(previous)
    registry.register(current)

    assertSame(previous, registry.remove("session-1"))
    assertSame(current, registry.findEntryByDeviceId("device-1"))
    assertNull(registry.findEntryBySessionId("session-1"))
  }

  @Test
  fun `sessions are looked up by user so membership changes reach every device`() {
    val phone = entry(sessionId = "session-1", deviceId = "device-1", userId = "user-1")
    val tablet = entry(sessionId = "session-2", deviceId = "device-2", userId = "user-1")
    val other = entry(sessionId = "session-3", deviceId = "device-3", userId = "user-2")
    listOf(phone, tablet, other).forEach { registry.register(it) }

    assertEquals(setOf("session-1", "session-2"), registry.findEntriesByUserId("user-1").map { it.sessionId }.toSet())
  }

  @Test
  fun `a session without a pong within the timeout is stale`() {
    val connectedAt = Instant.parse("2026-09-18T00:00:00Z")
    val entry = entry(sessionId = "session-1", deviceId = "device-1", userId = "user-1", connectedAt = connectedAt)

    assertTrue(entry.isStale(connectedAt.plusSeconds(91), Duration.ofSeconds(90)))

    entry.markPong(connectedAt.plusSeconds(60))

    assertFalse(entry.isStale(connectedAt.plusSeconds(91), Duration.ofSeconds(90)))
  }

  private fun entry(
    sessionId: String,
    deviceId: String,
    userId: String,
    connectedAt: Instant = Instant.now(),
  ): SessionEntry = SessionEntry(mock(WebSocketSession::class.java), sessionId, deviceId, userId, connectedAt)
}
