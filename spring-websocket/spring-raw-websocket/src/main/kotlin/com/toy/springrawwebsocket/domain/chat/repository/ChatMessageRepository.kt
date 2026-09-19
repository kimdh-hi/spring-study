package com.toy.springrawwebsocket.domain.chat.repository

import com.toy.springrawwebsocket.domain.chat.model.ChatMessage
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
  fun findByRoomIdAndIdGreaterThanOrderByIdAsc(roomId: String, id: Long, pageable: Pageable): List<ChatMessage>
}
