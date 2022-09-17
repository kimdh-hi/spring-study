package com.lecture.springbatchbasic.core.repository

import com.lecture.springbatchbasic.core.domain.PlainText
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PlainTextRepository: JpaRepository<PlainText, Long> {
  fun findBy(pageable: Pageable): Page<PlainText>
}