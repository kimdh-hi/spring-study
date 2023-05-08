package com.toy.jpaemoji.dto

import com.toy.jpaemoji.domain.Emoji

data class EmojiDto(
  val id: String? = null,
  val emoji: String
) {

  companion object {
    fun of(emoji: Emoji) = EmojiDto(id = emoji.id, emoji = emoji.emoji)
  }

  fun toEntity() = Emoji(emoji = this.emoji)
}