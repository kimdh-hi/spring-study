package com.toy.springrawwebsocket.application.chat

import com.toy.springrawwebsocket.domain.chat.model.ChatMessage
import com.toy.springrawwebsocket.domain.chat.repository.ChatMessageRepository
import com.toy.springrawwebsocket.domain.chat.repository.RoomUserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private const val MAX_LIMIT = 200

@Service
class ChatMessageQueryService(
  private val messageRepository: ChatMessageRepository,
  private val roomUserRepository: RoomUserRepository,
) {
  @Transactional(readOnly = true)
  fun findRoomMessages(roomId: String, userId: String, afterId: Long, limit: Int): List<ChatMessage>? {
    if (!roomUserRepository.existsByRoomIdAndUserId(roomId, userId)) return null

    return messageRepository.findByRoomIdAndIdGreaterThanOrderByIdAsc(
      roomId,
      afterId,
      PageRequest.of(0, limit.coerceIn(1, MAX_LIMIT)),
    )
  }
}
