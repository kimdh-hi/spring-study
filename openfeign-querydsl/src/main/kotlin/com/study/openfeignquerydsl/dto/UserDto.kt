package com.study.openfeignquerydsl.dto

import com.querydsl.core.annotations.QueryProjection
import com.study.openfeignquerydsl.domain.Email

data class UserDto @QueryProjection constructor(
  val id: String,
  val name: String,
  val team: TeamDto,
//  val email: Email
) {

  val additionalData: String? = null
}


data class TeamDto @QueryProjection constructor(
  val id: String,
  val name: String
)
