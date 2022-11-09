package com.lecture.userservice.model

import com.lecture.userservice.domain.User
import java.time.LocalDateTime

data class UserMeResponse(
  val id: Long,
  val name: String,
  val username: String,
  val profileUrl: String?,
  val createdAt: LocalDateTime
) {
  companion object {
    operator fun invoke(user: User) = with(user) {
      UserMeResponse(
        id = id!!,
        name = name,
        username = username,
        profileUrl = profileUrl,
        createdAt = createdAt!!
      )
    }
  }
}