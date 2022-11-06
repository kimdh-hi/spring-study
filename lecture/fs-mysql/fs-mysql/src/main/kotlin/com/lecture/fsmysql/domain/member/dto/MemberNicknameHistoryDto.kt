package com.lecture.fsmysql.domain.member.dto

import com.lecture.fsmysql.domain.member.entity.MemberNicknameHistory
import java.time.LocalDateTime

data class MemberNicknameHistoryDto(
  val id: Long,
  val memberId: Long,
  val nickname: String,
  val createdAt: LocalDateTime?
) {
  companion object {
    fun of(memberNicknameHistory: MemberNicknameHistory) = MemberNicknameHistoryDto(
      id = memberNicknameHistory.id!!,
      memberId = memberNicknameHistory.memberId,
      nickname = memberNicknameHistory.nickname,
      createdAt = memberNicknameHistory.createdAt
    )
  }
}