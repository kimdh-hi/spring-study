package com.toy.springstomp.infra.stomp

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.web.socket.WebSocketSession
import kotlin.test.assertNull
import kotlin.test.assertSame

class SessionRegistryTest {
  private val registry = SessionRegistry()

  @Test
  fun `registering the same device reports the replaced session`() {
    val previous = entry(sessionId = "session-1", deviceId = "device-1")
    val current = entry(sessionId = "session-2", deviceId = "device-1")
    registry.register(previous)

    assertSame(previous, registry.register(current))
    assertSame(current, registry.findEntryByDeviceId("device-1"))
  }

  @Test
  fun `registering a different device does not report a replaced session`() {
    val phone = entry(sessionId = "session-1", deviceId = "device-1")
    val tablet = entry(sessionId = "session-2", deviceId = "device-2")
    registry.register(phone)

    assertNull(registry.register(tablet))
  }

  @Test
  fun `removing a replaced session keeps the current device mapping`() {
    val previous = entry(sessionId = "session-1", deviceId = "device-1")
    val current = entry(sessionId = "session-2", deviceId = "device-1")
    registry.register(previous)
    registry.register(current)

    assertSame(previous, registry.remove("session-1"))
    assertSame(current, registry.findEntryByDeviceId("device-1"))
    assertNull(registry.remove("session-1"))
  }

  private fun entry(sessionId: String, deviceId: String): SessionEntry =
    SessionEntry(mock(WebSocketSession::class.java), sessionId, deviceId)
}
