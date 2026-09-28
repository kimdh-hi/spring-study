package com.toy.springstomp.infra.stomp

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class SessionRegistry {
  private val deviceIdsBySessionId = ConcurrentHashMap<String, String>()
  private val sessionIdsByDeviceId = ConcurrentHashMap<String, String>()

  /** Returns the session id previously bound to the same device, if it was a different session. */
  fun register(sessionId: String, deviceId: String): String? {
    deviceIdsBySessionId[sessionId] = deviceId

    return sessionIdsByDeviceId.put(deviceId, sessionId)?.takeUnless { it == sessionId }
  }

  fun remove(sessionId: String) {
    val deviceId = deviceIdsBySessionId.remove(sessionId) ?: return
    sessionIdsByDeviceId.remove(deviceId, sessionId)
  }

  fun findSessionIdByDeviceId(deviceId: String): String? = sessionIdsByDeviceId[deviceId]
}
