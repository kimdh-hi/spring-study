package com.toy.jpacustomgenerator.vo

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class UserResponseVO @QueryProjection constructor(
  val id: String,
  val name: String,
  val createdDate: LocalDateTime
)