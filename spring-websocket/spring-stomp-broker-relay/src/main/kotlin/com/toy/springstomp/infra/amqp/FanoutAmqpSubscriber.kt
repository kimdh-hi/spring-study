package com.toy.springstomp.infra.amqp

import com.toy.springstomp.infra.amqp.dto.DuplicateSessionMessage
import com.toy.springstomp.infra.stomp.SessionCloser
import com.toy.springstomp.infra.stomp.SessionRegistry
import com.toy.springstomp.infra.stomp.error.ErrorCode
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
class FanoutAmqpSubscriber(
  private val localSessionRegistry: SessionRegistry,
  private val sessionCloser: SessionCloser,
  private val jsonMapper: JsonMapper,
) {
  @RabbitListener(queues = ["#{duplicateSessionQueue.name}"])
  fun onDuplicateSession(body: String) {
    val duplicateSession = jsonMapper.readValue(body, DuplicateSessionMessage::class.java)
    val sessionId = localSessionRegistry.findSessionIdByDeviceId(duplicateSession.deviceId) ?: return
    if (sessionId == duplicateSession.sessionId) return
    sessionCloser.close(sessionId, ErrorCode.DUPLICATE_SESSION)
  }
}
