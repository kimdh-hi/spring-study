package com.toy.springkotlin.controller.dto

import com.toy.springkotlin.entity.User
import jakarta.validation.constraints.NotBlank
import java.time.Instant
import java.time.LocalDateTime

data class UserSaveV1Request(
  @field:NotBlank
  val name: String,
  @field:NotBlank
  val companyId: String,
)

data class UserSaveRequest(
  @field:NotBlank
  val name: UserName,
  @field:NotBlank
  val companyId: CompanyId,
)

@JvmInline
value class UserName(
//  @field:NotBlank // not work
  val value: String,
)

@JvmInline
value class CompanyId(
//  @field:NotBlank
  val value: String,
)

data class UserResponse(
  val userId: String,
  val name: String,
  val createdAt: LocalDateTime,
)
