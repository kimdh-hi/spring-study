package com.lecture.fsmysql.domain.member.entity

import java.time.LocalDate
import java.time.LocalDateTime

class Member(
  var id: Long? = null,
  var nickname: String,
  var email: String,
  val birthday: LocalDate,
  var createdAt: LocalDateTime? = null
) {
  init {
    createdAt = createdAt ?: LocalDateTime.now()
  }
}