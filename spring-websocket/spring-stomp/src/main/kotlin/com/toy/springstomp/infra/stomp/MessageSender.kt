package com.toy.springstomp.infra.stomp

import com.toy.springstomp.domain.chat.model.ChatMessage
import com.toy.springstomp.infra.redis.RedisChannels
import com.toy.springstomp.infra.redis.RedisPublisher
import com.toy.springstomp.infra.stomp.constants.Destinations
import com.toy.springstomp.infra.stomp.dto.MessageFrame
import org.springframework.context.annotation.Lazy
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class MessageSender(
  @Lazy private val messagingTemplate: SimpMessagingTemplate,
  private val publisher: RedisPublisher,
) {
  fun sendToRoom(message: ChatMessage) {
    publisher.publish(RedisChannels.ROOM_MESSAGE, MessageFrame.from(message))
  }

  fun sendToRoomLocally(frame: MessageFrame) {
    messagingTemplate.convertAndSend(Destinations.roomTopic(frame.roomId), frame)
  }
}
