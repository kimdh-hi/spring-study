package com.toy.emailauthentication.vo

data class SignupRequestVO (
  val username: String,
  val password: String
)

data class SignupResponseVO (
  val username: String,
)

data class SendResetPasswordAuthenticationMailVO(
  val username: String
)

data class PasswordResetRequestVO(
  val oldPassword: String,
  val newPassword: String,
)