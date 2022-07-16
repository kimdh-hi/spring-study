package com.toy.springquerydsl.vo

import com.querydsl.core.annotations.QueryProjection
import com.toy.springquerydsl.common.NoArg

@NoArg
data class UserResponseVO @QueryProjection constructor(
  val username: String,
  val age: Int,
)