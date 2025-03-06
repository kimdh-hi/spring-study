package com.toy.springkotlin.controller.dto

import jakarta.validation.constraints.NotBlank

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
