package com.toy.jpaemoji.service

import com.toy.jpaemoji.dto.EmojiDto
import com.toy.jpaemoji.repository.EmojiRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class EmojiService(
  private val emojiRepository: EmojiRepository
) {

  @Transactional
  fun save(dto: EmojiDto): EmojiDto {
    val emoji = dto.toEntity()
    val savedEmoji = emojiRepository.save(emoji)
    return EmojiDto.of(savedEmoji)
  }

  fun getAll() = emojiRepository.findAll()
    .map { EmojiDto.of(it) }
}