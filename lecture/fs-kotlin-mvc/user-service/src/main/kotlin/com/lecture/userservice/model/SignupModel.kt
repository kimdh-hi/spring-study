package com.lecture.userservice.model

import com.lecture.userservice.domain.User
import java.time.LocalDateTime

data class SignupRequest(
  val username: String,
  val password: String,
  val name: String
)

data class SignupResponse(
  val id: Long,
  val username: String,
  val name: String,
  val createdAt: LocalDateTime,
) {
  companion object {
    operator fun invoke(user: User) = with(user) {
      SignupResponse(
        id = id!!,
        username = username,
        name = name,
        createdAt = createdAt!!
      )
    }
  }
}