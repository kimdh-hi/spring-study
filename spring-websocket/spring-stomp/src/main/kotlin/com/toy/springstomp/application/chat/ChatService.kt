package com.toy.springstomp.application.chat

import com.toy.springstomp.domain.chat.model.ChatMessage
import com.toy.springstomp.domain.chat.repository.ChatMessageRepository
import com.toy.springstomp.infra.stomp.MessageSender
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ChatService(
  private val roomUserService: RoomUserService,
  private val messageSender: MessageSender,
  private val messageRepository: ChatMessageRepository,
) {
  fun send(userId: String, deviceId: String, roomId: String, text: String) {
    val trimmed = text.trim()
    if (trimmed.isEmpty() || !roomUserService.isMember(roomId, userId)) return
    val message = messageRepository.save(ChatMessage(roomId, userId, deviceId, trimmed, Instant.now()))
    messageSender.sendToRoom(message)
  }
}
