package com.toy.springrawwebsocket.infra.redis

import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Component

@Component
class RoomChannelRegistrar(
  private val listenerContainer: RedisMessageListenerContainer,
  private val roomFrameListener: RoomFrameListener,
) {
  fun listen(roomId: String) =
    listenerContainer.addMessageListener(roomFrameListener, ChannelTopic(RedisChannels.room(roomId)))

  fun stopListening(roomId: String) =
    listenerContainer.removeMessageListener(roomFrameListener, ChannelTopic(RedisChannels.room(roomId)))
}
