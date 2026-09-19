package com.toy.springrawwebsocket.infra.ws

import com.toy.springrawwebsocket.domain.chat.model.DeviceSession
import org.springframework.web.socket.WebSocketSession
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class SessionEntry(
  val session: WebSocketSession,
  val sessionId: String,
  val deviceId: String,
  val userId: String,
  connectedAt: Instant,
) {
  private val subscribedRoomIds: MutableSet<String> = ConcurrentHashMap.newKeySet()
  private val lastPongAtMillis = AtomicLong(connectedAt.toEpochMilli())

  fun subscribe(roomId: String): Boolean = subscribedRoomIds.add(roomId)

  fun unsubscribe(roomId: String): Boolean = subscribedRoomIds.remove(roomId)

  fun isSubscribed(roomId: String): Boolean = subscribedRoomIds.contains(roomId)

  fun subscribedRooms(): Set<String> = subscribedRoomIds.toSet()

  fun markPong(at: Instant) = lastPongAtMillis.set(at.toEpochMilli())

  fun isStale(now: Instant, timeout: Duration): Boolean =
    now.toEpochMilli() - lastPongAtMillis.get() > timeout.toMillis()

  fun isSameSession(other: SessionEntry): Boolean = sessionId == other.sessionId

  fun toDeviceSession(): DeviceSession = DeviceSession(sessionId, deviceId, userId)
}
