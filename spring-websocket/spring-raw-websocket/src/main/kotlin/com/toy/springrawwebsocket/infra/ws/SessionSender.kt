package com.toy.springrawwebsocket.infra.ws

import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketMessage

@Component
class SessionSender {
  fun send(entry: SessionEntry, message: WebSocketMessage<*>) {
    runCatching { entry.session.sendMessage(message) }.onFailure { close(entry, CloseStatus.SESSION_NOT_RELIABLE) }
  }

  fun close(entry: SessionEntry, status: CloseStatus) {
    runCatching { entry.session.close(status) }
  }
}
