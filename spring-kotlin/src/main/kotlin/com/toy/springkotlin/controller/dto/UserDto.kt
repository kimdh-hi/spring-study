package com.toy.springkotlin.controller.dto

data class UserSaveRequest(
  val userId: UserId,
  val companyId: CompanyId,
)

@JvmInline
value class UserId(
  val value: String
)

@JvmInline
value class CompanyId(
  val value: String
)
