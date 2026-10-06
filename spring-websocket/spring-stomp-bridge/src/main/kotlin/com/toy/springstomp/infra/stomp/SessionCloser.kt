package com.toy.springstomp.infra.stomp

import com.toy.springstomp.infra.stomp.error.ErrorCode
import com.toy.springstomp.infra.stomp.error.ErrorResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Lazy
import org.springframework.http.MediaType
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

/**
 * Closes a local session by sending it an ERROR frame; Spring closes the WebSocket right after writing an ERROR frame.
 */
@Component
class SessionCloser(
  @Lazy @Qualifier("clientOutboundChannel") private val clientOutboundChannel: MessageChannel,
  private val jsonMapper: JsonMapper,
) {
  fun close(sessionId: String, errorCode: ErrorCode) {
    val accessor = StompHeaderAccessor.create(StompCommand.ERROR).apply {
      this.sessionId = sessionId
      message = errorCode.message
      setContentType(MediaType.APPLICATION_JSON)
      setLeaveMutable(true)
    }
    val payload = jsonMapper.writeValueAsBytes(ErrorResponse.of(errorCode))
    clientOutboundChannel.send(MessageBuilder.createMessage(payload, accessor.messageHeaders))
  }
}
