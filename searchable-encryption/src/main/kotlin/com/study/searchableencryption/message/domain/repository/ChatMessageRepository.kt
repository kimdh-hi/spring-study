package com.study.searchableencryption.message.domain.repository

import com.study.searchableencryption.message.domain.model.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom

interface ChatMessageRepositoryCustom {
  fun findIdsMatchingAllTokens(roomId: Long, tokens: Collection<String>): List<Long>

  fun findAllByRoom(roomId: Long): List<ChatMessage>

  fun findAllByCipherLike(roomId: Long, keyword: String): List<ChatMessage>
}
