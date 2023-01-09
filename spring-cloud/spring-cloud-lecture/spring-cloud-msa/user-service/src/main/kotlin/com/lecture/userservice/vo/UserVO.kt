package com.lecture.userservice.vo

import com.lecture.userservice.domain.User
import java.util.UUID

data class UserSaveRequestVO(
  val name: String,
  val email: String,
  val password: String
) {
  fun toEntity(encPassword: String) = User(
      email = email,
      name = name,
      userId = UUID.randomUUID().toString(),
      password = encPassword
  )
}

data class UserResponseVO(
  val id: String?,
  val name: String,
  val email: String,
  val userId: String
) {
  companion object {
    fun of(user: User) = UserResponseVO(
      id = user.userId,
      name = user.name,
      email = user.email,
      userId = user.userId
    )
  }
}