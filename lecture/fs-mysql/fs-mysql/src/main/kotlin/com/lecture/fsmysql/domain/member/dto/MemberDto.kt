package com.lecture.fsmysql.domain.member.dto

import com.lecture.fsmysql.domain.member.entity.Member
import java.time.LocalDate

data class MemberDto(
  val id: Long,
  val email: String,
  val nickname: String,
  val birthday: LocalDate
) {
  companion object {
    fun of(member: Member) = with(member) {
      MemberDto(
        id = id!!, email = email, nickname = nickname, birthday = birthday
      )
    }
  }
}