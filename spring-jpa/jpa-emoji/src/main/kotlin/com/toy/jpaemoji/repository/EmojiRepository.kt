package com.toy.jpaemoji.repository

import com.toy.jpaemoji.domain.Emoji
import org.springframework.data.jpa.repository.JpaRepository

interface EmojiRepository: JpaRepository<Emoji, String> {
}