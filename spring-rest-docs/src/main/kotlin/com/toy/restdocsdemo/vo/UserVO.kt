package com.toy.restdocsdemo.vo

import com.toy.restdocsdemo.domain.User
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class UserCreateRequestVO(
  val email: String,
  val name: String,
) {
  fun toEntity() = User(
    email = email,
    name = name
  )
}

data class UserUpdateRequestVO(
  @field:NotBlank
  val name: String
)

data class UserResponseVO(
  val id: Long,
  val email: String,
  val name: String,
  val createdAt: LocalDateTime,
) {
  companion object {
    fun of(user: User) = with(user) {
      UserResponseVO(
        id = id!!,
        email = email,
        name = name,
        createdAt = createdAt!!
      )
    }
  }
}