package com.toy.springrawwebsocket.ui.api.dto

import com.toy.springrawwebsocket.domain.chat.model.ChatMessage

data class ChatMessageResponse(
  val id: Long,
  val roomId: String,
  val senderUserId: String,
  val senderDeviceId: String,
  val text: String,
  val at: Long,
) {
  companion object {
    fun from(message: ChatMessage): ChatMessageResponse = ChatMessageResponse(
      id = requireNotNull(message.id),
      roomId = message.roomId,
      senderUserId = message.senderUserId,
      senderDeviceId = message.senderDeviceId,
      text = message.text,
      at = message.at.toEpochMilli(),
    )
  }
}
