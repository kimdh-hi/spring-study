package com.toy.springstomp.ui.stomp

import com.toy.springstomp.infra.redis.RedisChannels
import com.toy.springstomp.infra.redis.RedisPublisher
import com.toy.springstomp.infra.redis.dto.DuplicateSessionMessage
import com.toy.springstomp.infra.stomp.SessionCloser
import com.toy.springstomp.infra.stomp.SessionRegistry
import com.toy.springstomp.infra.stomp.error.ErrorCode
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class SessionLifecycleInterceptor(
  private val localSessionRegistry: SessionRegistry,
  private val sessionCloser: SessionCloser,
  private val publisher: RedisPublisher,
) : ChannelInterceptor {
  /**
   * 메시지 처리 전 호출, 여기서 예외를 던지면 메시지 처리 중단.
   * DISCONNECT 수신 시 세션 제거.
   */
  override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
    val accessor = StompHeaderAccessor.wrap(message)
    if (accessor.command == StompCommand.DISCONNECT) accessor.sessionId?.let(localSessionRegistry::remove)

    return message
  }

  /**
   * 모든 preSend를 통과한 메시지에 대해서만 호출, 처리 완료 시점은 아님.
   * 거절되지 않은 CONNECT만 세션 등록.
   * preSend에서 처리하면 이후 거절될 연결 때문에 기존 세션이 먼저 끊길 수 있음.
   */
  override fun postSend(message: Message<*>, channel: MessageChannel, sent: Boolean) {
    val accessor = StompHeaderAccessor.wrap(message)
    if (!sent || accessor.command != StompCommand.CONNECT) return
    val sessionId = accessor.sessionId ?: return
    val user = accessor.user as? ChatUser ?: return
    localSessionRegistry.register(sessionId, user.deviceId)?.let { sessionCloser.close(it, ErrorCode.DUPLICATE_SESSION) }
    publisher.publish(RedisChannels.DUPLICATE_SESSION, DuplicateSessionMessage(user.deviceId, sessionId))
  }
}
