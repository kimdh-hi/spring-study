package com.lecture.springbatchbasic.core.repository

import com.lecture.springbatchbasic.core.domain.PlainText
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PlainTextRepository: JpaRepository<PlainText, Long> {
  override fun findAll(pageable: Pageable): Page<PlainText>
}