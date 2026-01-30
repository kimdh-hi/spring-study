package com.study.openfeignquerydsl.dto

import com.querydsl.core.annotations.QueryProjection
import com.study.openfeignquerydsl.domain.Email
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseDto

data class UserDto @QueryProjection constructor(
  val id: String,
  val name: String,
  val team: TeamDto,
  val email: Email
) : BaseDto() {

  val additionalData: String? = null
}


data class TeamDto @QueryProjection constructor(
  val id: String,
  val name: String
)

data class UserDto2(
  val id: String,
  val name: String,
) {
  @QueryProjection constructor(
    id: String,
    name: String,
    email: Email
  ) : this(
    id = id,
    name = name,
  )
}
