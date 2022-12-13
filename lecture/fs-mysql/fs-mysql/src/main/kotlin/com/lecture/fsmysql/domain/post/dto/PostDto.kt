package com.lecture.fsmysql.domain.post.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime

data class PostDto(
  val id: Long,
  val contents: String,
  val createdAt: LocalDateTime,
  val likeCount: Long
)

data class DailyPostCountRequestDto(
  val memberId: Long,

  @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  val firstDate: LocalDate,

  @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  val lastDate: LocalDate
)


data class DailyPostCountResponseDto(
  val memberId: Long,
  val date: LocalDate,
  val postCount: Long
)