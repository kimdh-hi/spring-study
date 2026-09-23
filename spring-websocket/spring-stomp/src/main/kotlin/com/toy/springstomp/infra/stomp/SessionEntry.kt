package com.toy.springstomp.infra.stomp

import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession

class SessionEntry(
  val session: WebSocketSession,
  val sessionId: String,
  val deviceId: String,
) {
  fun isSameSession(other: SessionEntry): Boolean = sessionId == other.sessionId

  fun close(status: CloseStatus) {
    runCatching { session.close(status) }
  }
}
