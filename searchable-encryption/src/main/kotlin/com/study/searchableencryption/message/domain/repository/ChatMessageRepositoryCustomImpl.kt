package com.study.searchableencryption.message.domain.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.study.searchableencryption.message.domain.model.ChatMessage
import com.study.searchableencryption.message.domain.model.QChatMessage.Companion.chatMessage
import com.study.searchableencryption.message.domain.model.QMessageToken.Companion.messageToken
import org.springframework.stereotype.Repository

@Repository
class ChatMessageRepositoryCustomImpl(
  private val query: JPAQueryFactory,
) : ChatMessageRepositoryCustom {

  override fun findIdsMatchingAllTokens(roomId: Long, tokens: Collection<String>): List<Long> =
    query.select(chatMessage.id)
      .from(chatMessage)
      .join(messageToken).on(messageToken.messageId.eq(chatMessage.id))
      .where(chatMessage.roomId.eq(roomId), messageToken.token.`in`(tokens))
      .groupBy(chatMessage.id)
      .having(messageToken.token.countDistinct().eq(tokens.size.toLong()))
      .fetch()

  override fun findAllByRoom(roomId: Long): List<ChatMessage> =
    query.selectFrom(chatMessage)
      .where(chatMessage.roomId.eq(roomId))
      .orderBy(chatMessage.sentAt.asc())
      .fetch()

  override fun findAllByCipherLike(roomId: Long, keyword: String): List<ChatMessage> =
    query.selectFrom(chatMessage)
      .where(chatMessage.roomId.eq(roomId), chatMessage.content.contains(keyword))
      .fetch()
}
