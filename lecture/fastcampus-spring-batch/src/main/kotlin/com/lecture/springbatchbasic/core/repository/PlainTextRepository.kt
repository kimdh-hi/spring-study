package com.lecture.springbatchbasic.repository

import com.lecture.springbatchbasic.domain.PlainText
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface PlainTextRepository: JpaRepository<PlainText, Long> {
  fun findBy(pageable: Pageable): Page<PlainText>
}