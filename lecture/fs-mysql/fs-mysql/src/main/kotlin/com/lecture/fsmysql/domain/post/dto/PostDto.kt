package com.lecture.fsmysql.domain.post.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

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