package com.toy.springrawwebsocket.infra.redis

import com.toy.springrawwebsocket.infra.redis.dto.DuplicateSessionMessage
import com.toy.springrawwebsocket.infra.redis.dto.RoomUserChangedMessage
import com.toy.springrawwebsocket.infra.ws.RoomSubscriptions
import com.toy.springrawwebsocket.infra.ws.SessionRegistry
import com.toy.springrawwebsocket.infra.ws.SessionSender
import com.toy.springrawwebsocket.infra.ws.constants.DUPLICATE_SESSION
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
  private val sessionSender: SessionSender,
  private val roomSubscriptions: RoomSubscriptions,
  private val jsonMapper: JsonMapper,
) : MessageListener {
  @PostConstruct
  fun subscribe() = listenerContainer.addMessageListener(
    this,
    listOf(ChannelTopic(RedisChannels.DUPLICATE_SESSION), ChannelTopic(RedisChannels.ROOM_USER)),
  )

  override fun onMessage(message: Message, pattern: ByteArray?) {
    val body = String(message.body)
    when (String(message.channel)) {
      RedisChannels.DUPLICATE_SESSION -> onDuplicateSession(body)
      RedisChannels.ROOM_USER -> onRoomUserChanged(body)
    }
  }

  private fun onDuplicateSession(body: String) {
    val duplicateSession = jsonMapper.readValue(body, DuplicateSessionMessage::class.java)
    val entry = localSessionRegistry.findEntryByDeviceId(duplicateSession.deviceId) ?: return
    if (entry.sessionId == duplicateSession.sessionId) return
    sessionSender.close(entry, DUPLICATE_SESSION)
  }

  private fun onRoomUserChanged(body: String) {
    val roomUserChanged = jsonMapper.readValue(body, RoomUserChangedMessage::class.java)
    roomSubscriptions.applyRoomUserChangeLocally(
      roomUserChanged.userId,
      roomUserChanged.roomId,
      roomUserChanged.joined,
    )
  }
}
