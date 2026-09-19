package com.toy.springrawwebsocket.infra.ws

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class SessionRegistry {
  private val entriesBySessionId = ConcurrentHashMap<String, SessionEntry>()
  private val entriesByDeviceId = ConcurrentHashMap<String, SessionEntry>()
  private val sessionIdsByRoomId = ConcurrentHashMap<String, MutableSet<String>>()

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

  fun findEntryBySessionId(sessionId: String): SessionEntry? = entriesBySessionId[sessionId]

  fun findEntryByDeviceId(deviceId: String): SessionEntry? = entriesByDeviceId[deviceId]

  fun findEntries(): List<SessionEntry> = entriesBySessionId.values.toList()

  fun findEntriesByUserId(userId: String): List<SessionEntry> = entriesBySessionId.values.filter { it.userId == userId }

  fun findEntriesByRoomId(roomId: String): List<SessionEntry> =
    sessionIdsByRoomId[roomId].orEmpty().mapNotNull { entriesBySessionId[it] }

  fun isSubscribed(sessionId: String, roomId: String): Boolean =
    entriesBySessionId[sessionId]?.isSubscribed(roomId) == true

  fun subscribe(entry: SessionEntry, roomId: String): Boolean {
    entry.subscribe(roomId)
    var firstLocalSubscriber = false
    sessionIdsByRoomId.compute(roomId) { _, sessionIds ->
      val subscribers = sessionIds ?: ConcurrentHashMap.newKeySet<String>().also { firstLocalSubscriber = true }
      subscribers.add(entry.sessionId)
      subscribers
    }

    return firstLocalSubscriber
  }

  fun unsubscribe(entry: SessionEntry, roomId: String): Boolean {
    entry.unsubscribe(roomId)
    var lastLocalSubscriber = false
    sessionIdsByRoomId.computeIfPresent(roomId) { _, sessionIds ->
      sessionIds.remove(entry.sessionId)
      sessionIds.takeIf { it.isNotEmpty() } ?: null.also { lastLocalSubscriber = true }
    }

    return lastLocalSubscriber
  }
}
