package com.lecture.snsapp.vo

import com.lecture.snsapp.domain.User
import com.lecture.snsapp.domain.UserRole
import java.sql.Timestamp

data class UserJoinRequestVO(
  val username: String,
  val password: String
)

data class UserLoginRequestVO(
  val username: String,
  val password: String
)

data class UserResponseVO(
  val id: Long,
  val username: String,
  val userRole: UserRole? = null,
  val registerAt: Timestamp? = null,
  val updatedAt: Timestamp? = null,
  val deletedAt: Timestamp? = null,
) {
  companion object {
    fun of(user: User) = UserResponseVO(
      id = user.id!!,
      username = user.username, userRole = user.role,
      registerAt = user.registerAt, updatedAt = user.updatedAt, deletedAt = user.deletedAt
    )
  }
}

data class LoginResponseVO(
  val token: String
)