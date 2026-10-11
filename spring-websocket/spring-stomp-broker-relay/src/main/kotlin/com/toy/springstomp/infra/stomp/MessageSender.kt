package com.toy.springstomp.infra.stomp

import com.toy.springstomp.domain.chat.model.ChatMessage
import com.toy.springstomp.infra.stomp.constants.Destinations
import com.toy.springstomp.infra.stomp.dto.MessageFrame
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class MessageSender(
  private val messagingTemplate: SimpMessagingTemplate,
) {
  fun sendToRoom(message: ChatMessage) {
    messagingTemplate.convertAndSend(Destinations.roomTopic(message.roomId), MessageFrame.from(message))
  }
}
