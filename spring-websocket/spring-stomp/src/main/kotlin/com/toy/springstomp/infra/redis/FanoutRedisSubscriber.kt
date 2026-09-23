package com.toy.springstomp.infra.redis

import com.toy.springstomp.infra.redis.dto.DuplicateSessionMessage
import com.toy.springstomp.infra.stomp.MessageSender
import com.toy.springstomp.infra.stomp.SessionRegistry
import com.toy.springstomp.infra.stomp.constants.DUPLICATE_SESSION
import com.toy.springstomp.infra.stomp.dto.MessageFrame
import jakarta.annotation.PostConstruct
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
class FanoutRedisSubscriber(
  private val listenerContainer: RedisMessageListenerContainer,
  private val localSessionRegistry: SessionRegistry,
  private val messageSender: MessageSender,
  private val jsonMapper: JsonMapper,
) : MessageListener {
  @PostConstruct
  fun subscribe() = listenerContainer.addMessageListener(
    this,
    listOf(ChannelTopic(RedisChannels.DUPLICATE_SESSION), ChannelTopic(RedisChannels.ROOM_MESSAGE)),
  )

  override fun onMessage(message: Message, pattern: ByteArray?) {
    val body = String(message.body)
    when (String(message.channel)) {
      RedisChannels.DUPLICATE_SESSION -> onDuplicateSession(body)
      RedisChannels.ROOM_MESSAGE -> onRoomMessage(body)
    }
  }

  private fun onDuplicateSession(body: String) {
    val duplicateSession = jsonMapper.readValue(body, DuplicateSessionMessage::class.java)
    val entry = localSessionRegistry.findEntryByDeviceId(duplicateSession.deviceId) ?: return
    if (entry.sessionId == duplicateSession.sessionId) return
    entry.close(DUPLICATE_SESSION)
  }

  private fun onRoomMessage(body: String) =
    messageSender.sendToRoomLocally(jsonMapper.readValue(body, MessageFrame::class.java))
}
