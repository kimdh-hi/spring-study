package com.toy.springexposed.vo

import com.toy.springexposed.domain.Member
import com.toy.springexposed.domain.Team
import org.jetbrains.exposed.sql.ResultRow

data class MemberResponseVO(
  val teamId: String,
  val teamName: String,
  val userId: String,
  val memberName: String
) {
  companion object {
    fun of(resultRow: ResultRow) = MemberResponseVO(
      teamId = resultRow[Team.id].toString(),
      teamName = resultRow[Team.name],
      userId = resultRow[Member.id].toString(),
      memberName = resultRow[Member.name]
    )
  }
}