package com.toy.springstomp.infra.stomp.error

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.messaging.Message
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler
import tools.jackson.databind.json.JsonMapper

/**
 * Renders every ERROR frame as a JSON [ErrorResponse] and copies its message into the `message` header.
 * Exceptions thrown from inbound channel interceptors arrive wrapped, so the cause carries the [StompException].
 */
@Component
class StompErrorHandler(
  private val jsonMapper: JsonMapper,
) : StompSubProtocolErrorHandler() {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun handleInternal(
    errorHeaderAccessor: StompHeaderAccessor,
    errorPayload: ByteArray,
    cause: Throwable?,
    clientHeaderAccessor: StompHeaderAccessor?,
  ): Message<ByteArray> {
    val response = ErrorResponse.of(errorCodeOf(cause, clientHeaderAccessor))
    errorHeaderAccessor.message = response.message
    errorHeaderAccessor.setContentType(MediaType.APPLICATION_JSON)

    return MessageBuilder.createMessage(jsonMapper.writeValueAsBytes(response), errorHeaderAccessor.messageHeaders)
  }

  private fun errorCodeOf(cause: Throwable?, clientHeaderAccessor: StompHeaderAccessor?): ErrorCode {
    val stompException = generateSequence(cause) { it.cause }.filterIsInstance<StompException>().firstOrNull()
    if (stompException != null) {
      log.warn(
        "stomp error: code={}, command={}, destination={}",
        stompException.errorCode,
        clientHeaderAccessor?.command,
        clientHeaderAccessor?.destination,
      )
      return stompException.errorCode
    }
    log.error("unexpected stomp error: command={}", clientHeaderAccessor?.command, cause)

    return ErrorCode.UNKNOWN
  }
}
