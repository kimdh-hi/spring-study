package com.study.searchableencryption.message.ui

import com.study.searchableencryption.message.domain.model.ChatMessage
import java.time.LocalDateTime

data class SendMessageRequest(
  val senderId: Long,
  val content: String,
)

data class ChatMessageResponse(
  val id: Long,
  val roomId: Long,
  val senderId: Long,
  val content: String,
  val sentAt: LocalDateTime,
) {
  companion object {
    fun from(message: ChatMessage) = ChatMessageResponse(
      id = message.id!!,
      roomId = message.roomId,
      senderId = message.senderId,
      content = message.content,
      sentAt = message.sentAt,
    )
  }
}
