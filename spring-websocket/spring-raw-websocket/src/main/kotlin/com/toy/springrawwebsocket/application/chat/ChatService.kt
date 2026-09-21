package com.toy.springrawwebsocket.application.chat

import com.toy.springrawwebsocket.domain.chat.model.ChatMessage
import com.toy.springrawwebsocket.domain.chat.model.DeviceSession
import com.toy.springrawwebsocket.domain.chat.repository.ChatMessageRepository
import com.toy.springrawwebsocket.infra.ws.MessageSender
import com.toy.springrawwebsocket.infra.ws.SessionRegistry
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ChatService(
  private val localSessionRegistry: SessionRegistry,
  private val messageSender: MessageSender,
  private val messageRepository: ChatMessageRepository,
) {
  fun send(session: DeviceSession, roomId: String, text: String): Boolean {
    val trimmed = text.trim()
    if (trimmed.isEmpty()) return false
    if (!localSessionRegistry.isSubscribed(session.sessionId, roomId)) return false
    val message = messageRepository.save(ChatMessage(roomId, session.userId, session.deviceId, trimmed, Instant.now()))
    messageSender.sendToRoom(message)

    return true
  }
}
