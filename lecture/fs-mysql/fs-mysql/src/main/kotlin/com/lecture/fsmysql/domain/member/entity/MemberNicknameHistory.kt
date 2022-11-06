package com.lecture.fsmysql.domain.member.entity

import java.time.LocalDateTime

class MemberNicknameHistory(
  var id: Long? = null,
  val memberId: Long,
  val nickname: String,
  var createdAt: LocalDateTime? = null
) {
  init {
    createdAt = LocalDateTime.now()
  }

  companion object {
    fun of(member: Member) = MemberNicknameHistory(
      memberId = member.id!!,
      nickname = member.nickname,
    )
  }

  override fun toString(): String {
    return "MemberNicknameHistory(id=$id, memberId=$memberId, nickname='$nickname', createdDate=$createdAt)"
  }
}