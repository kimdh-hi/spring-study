package com.toy.springstomp.application.chat

import com.toy.springstomp.domain.chat.model.ChatMessage
import com.toy.springstomp.domain.chat.repository.ChatMessageRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private const val MAX_LIMIT = 200

@Service
class ChatMessageQueryService(
  private val messageRepository: ChatMessageRepository,
  private val roomUserService: RoomUserService,
) {
  @Transactional(readOnly = true)
  fun findRoomMessages(roomId: String, userId: String, afterId: Long, limit: Int): List<ChatMessage>? {
    if (!roomUserService.isMember(roomId, userId)) return null

    return messageRepository.findByRoomIdAndIdGreaterThanOrderByIdAsc(
      roomId,
      afterId,
      PageRequest.of(0, limit.coerceIn(1, MAX_LIMIT)),
    )
  }
}
