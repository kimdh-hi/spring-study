package com.toy.springstomp.infra.stomp.dto

import com.toy.springstomp.domain.chat.model.ChatMessage

data class ChatSendRequest(
  val text: String? = null,
)

data class MessageFrame(
  val id: Long,
  val roomId: String,
  val senderUserId: String,
  val senderDeviceId: String,
  val text: String,
  val at: Long,
) {
  companion object {
    fun from(message: ChatMessage): MessageFrame = MessageFrame(
      id = requireNotNull(message.id),
      roomId = message.roomId,
      senderUserId = message.senderUserId,
      senderDeviceId = message.senderDeviceId,
      text = message.text,
      at = message.at.toEpochMilli(),
    )
  }
}
