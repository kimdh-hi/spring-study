package com.toy.springstomp.infra.stomp

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class SessionRegistry {
  private val entriesBySessionId = ConcurrentHashMap<String, SessionEntry>()
  private val entriesByDeviceId = ConcurrentHashMap<String, SessionEntry>()

  fun register(entry: SessionEntry): SessionEntry? {
    entriesBySessionId[entry.sessionId] = entry
    val duplicatedDeviceEntry = entriesByDeviceId.put(entry.deviceId, entry)

    return duplicatedDeviceEntry?.takeUnless { it.isSameSession(entry) }
  }

  fun remove(sessionId: String): SessionEntry? {
    val entry = entriesBySessionId.remove(sessionId) ?: return null
    entriesByDeviceId.remove(entry.deviceId, entry)

    return entry
  }

  fun findEntryByDeviceId(deviceId: String): SessionEntry? = entriesByDeviceId[deviceId]
}
