package com.lecture.fsmysql.domain.member.dto

import java.time.LocalDate

data class MemberCreateCommand(
  val email: String,
  val nickname: String,
  val birthday: LocalDate
)