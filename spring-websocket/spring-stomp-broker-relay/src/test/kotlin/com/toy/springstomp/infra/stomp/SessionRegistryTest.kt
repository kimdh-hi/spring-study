package com.toy.springstomp.infra.stomp

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SessionRegistryTest {
  private val registry = SessionRegistry()

  @Test
  fun `registering the same device reports the replaced session`() {
    registry.register("session-1", "device-1")

    assertEquals("session-1", registry.register("session-2", "device-1"))
    assertEquals("session-2", registry.findSessionIdByDeviceId("device-1"))
  }

  @Test
  fun `registering a different device does not report a replaced session`() {
    registry.register("session-1", "device-1")

    assertNull(registry.register("session-2", "device-2"))
  }

  @Test
  fun `removing a replaced session keeps the current device mapping`() {
    registry.register("session-1", "device-1")
    registry.register("session-2", "device-1")

    registry.remove("session-1")

    assertEquals("session-2", registry.findSessionIdByDeviceId("device-1"))
  }

  @Test
  fun `removing the current session clears the device mapping`() {
    registry.register("session-1", "device-1")

    registry.remove("session-1")
    registry.remove("session-1")

    assertNull(registry.findSessionIdByDeviceId("device-1"))
  }
}
