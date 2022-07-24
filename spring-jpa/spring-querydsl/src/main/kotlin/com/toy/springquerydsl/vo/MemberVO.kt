package com.toy.springquerydsl.vo

import com.querydsl.core.annotations.QueryProjection
import com.toy.springquerydsl.common.NoArg

@NoArg
data class MemberResponseVO @QueryProjection constructor(
  val username: String,
  val age: Int,
)

data class MemberDetailsResponseVO @QueryProjection constructor(
  val userId: Long,
  val username: String,
  val age: Int,
  val teamId: Long,
  val teamName: String
)

data class MemberSearchVO(
  val username: String? = null,
  val age: Int? = null,
)
