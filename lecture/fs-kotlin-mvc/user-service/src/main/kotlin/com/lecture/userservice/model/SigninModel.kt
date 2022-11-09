package com.lecture.userservice.model

data class SignInRequest(
  val username: String,
  val password: String
)

data class SignInResponse(
  val token: String
)