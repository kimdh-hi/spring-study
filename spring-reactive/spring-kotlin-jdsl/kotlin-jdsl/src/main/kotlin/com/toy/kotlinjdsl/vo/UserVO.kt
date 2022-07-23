package com.toy.kotlinjdsl.vo

data class UserResponseV1VO(
  val id: String,
  val name: String,
  val username: String,
  val companyName: String,
)

data class UserResponseVO(
  val id: String,
  val name: String,
  val username: String,
  val companyName: String,
  val role: String
)

data class UserSearchVO(
  val roleId: String? = null,
  val keyword: String? = null,
)