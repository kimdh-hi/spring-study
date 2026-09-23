package com.toy.springstomp.domain.chat.repository

import com.toy.springstomp.domain.chat.model.ChatMessage
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
  fun findByRoomIdAndIdGreaterThanOrderByIdAsc(roomId: String, id: Long, pageable: Pageable): List<ChatMessage>
}
