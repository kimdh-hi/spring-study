package com.study.searchableencryption.message.application

import com.study.searchableencryption.message.domain.model.ChatMessage
import com.study.searchableencryption.message.domain.model.MessageToken
import com.study.searchableencryption.message.domain.repository.ChatMessageRepository
import com.study.searchableencryption.message.domain.repository.MessageTokenRepository
import com.study.searchableencryption.message.infra.BlindIndex
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatMessageService(
  private val chatMessageRepository: ChatMessageRepository,
  private val messageTokenRepository: MessageTokenRepository,
  private val blindIndex: BlindIndex,
) {

  @Transactional
  fun send(roomId: Long, senderId: Long, content: String): ChatMessage {
    val message = chatMessageRepository.save(ChatMessage(roomId, senderId, content))
    val messageTokens = blindIndex.tokens(content).map { MessageToken(message.id!!, it) }
    messageTokenRepository.saveAll(messageTokens)
    return message
  }

  @Transactional
  fun delete(messageId: Long) {
    messageTokenRepository.deleteByMessageId(messageId)
    chatMessageRepository.deleteById(messageId)
  }

  @Transactional(readOnly = true)
  fun search(roomId: Long, query: String): List<ChatMessage> {
    if (blindIndex.normalize(query).length < BlindIndex.MIN_QUERY_LENGTH) return emptyList()

    val searchTokens = blindIndex.tokens(query)
    val chatMessageIds = chatMessageRepository.findIdsMatchingAllTokens(roomId, searchTokens)

    return chatMessageRepository.findAllById(chatMessageIds)
      .filter { it.contains(query) }
      .sortedBy { it.sentAt }
  }

  @Transactional(readOnly = true)
  fun searchByFullScan(roomId: Long, query: String): List<ChatMessage> =
    chatMessageRepository.findAllByRoom(roomId).filter { it.contains(query) }

  @Transactional(readOnly = true)
  fun searchByLikeOnCipher(roomId: Long, query: String): List<ChatMessage> =
    chatMessageRepository.findAllByCipherLike(roomId, query)

  private fun ChatMessage.contains(query: String): Boolean =
    blindIndex.normalize(content).contains(blindIndex.normalize(query))
}
