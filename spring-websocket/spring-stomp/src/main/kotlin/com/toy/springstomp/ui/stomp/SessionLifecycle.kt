package com.toy.springstomp.ui.stomp

import com.toy.springstomp.infra.redis.RedisChannels
import com.toy.springstomp.infra.redis.RedisPublisher
import com.toy.springstomp.infra.redis.dto.DuplicateSessionMessage
import com.toy.springstomp.infra.stomp.SessionEntry
import com.toy.springstomp.infra.stomp.SessionRegistry
import com.toy.springstomp.infra.stomp.constants.DUPLICATE_SESSION
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.WebSocketHandlerDecorator
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory

@Component
class SessionLifecycle(
  private val localSessionRegistry: SessionRegistry,
  private val publisher: RedisPublisher,
) : WebSocketHandlerDecoratorFactory {
  override fun decorate(handler: WebSocketHandler): WebSocketHandler = object : WebSocketHandlerDecorator(handler) {
    override fun afterConnectionEstablished(session: WebSocketSession) {
      super.afterConnectionEstablished(session)
      val user = session.principal as ChatUser
      val entry = SessionEntry(session, session.id, user.deviceId)
      localSessionRegistry.register(entry)?.close(DUPLICATE_SESSION)
      publisher.publish(RedisChannels.DUPLICATE_SESSION, DuplicateSessionMessage(entry.deviceId, entry.sessionId))
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
      localSessionRegistry.remove(session.id)
      super.afterConnectionClosed(session, closeStatus)
    }
  }
}
