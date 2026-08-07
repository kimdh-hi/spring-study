package com.study.searchableencryption.message.domain.repository

import com.study.searchableencryption.message.domain.model.MessageToken
import org.springframework.data.jpa.repository.JpaRepository

interface MessageTokenRepository : JpaRepository<MessageToken, Long> {

  fun deleteByMessageId(messageId: Long)
}
