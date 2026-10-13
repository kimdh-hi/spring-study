package com.toy.springstomp.infra.redis

import com.toy.springstomp.infra.redis.dto.DuplicateSessionMessage
import com.toy.springstomp.infra.stomp.MessageSender
import com.toy.springstomp.infra.stomp.SessionCloser
import com.toy.springstomp.infra.stomp.SessionRegistry
import com.toy.springstomp.infra.stomp.error.ErrorCode
import com.toy.springstomp.infra.stomp.dto.MessageFrame
import org.springframework.data.redis.annotation.RedisListener
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
class FanoutRedisSubscriber(
  private val localSessionRegistry: SessionRegistry,
  private val sessionCloser: SessionCloser,
  private val messageSender: MessageSender,
  private val jsonMapper: JsonMapper,
) {
  @RedisListener(topic = RedisChannels.DUPLICATE_SESSION)
  fun onDuplicateSession(body: String) {
    val duplicateSession = jsonMapper.readValue(body, DuplicateSessionMessage::class.java)
    val sessionId = localSessionRegistry.findSessionIdByDeviceId(duplicateSession.deviceId) ?: return
    if (sessionId == duplicateSession.sessionId) return
    sessionCloser.close(sessionId, ErrorCode.DUPLICATE_SESSION)
  }

  @RedisListener(topic = RedisChannels.ROOM_MESSAGE)
  fun onRoomMessage(body: String) =
    messageSender.sendToRoomLocally(jsonMapper.readValue(body, MessageFrame::class.java))
}
