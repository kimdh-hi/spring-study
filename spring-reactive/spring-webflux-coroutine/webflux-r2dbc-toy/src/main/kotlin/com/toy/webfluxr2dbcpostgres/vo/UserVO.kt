package com.toy.webfluxr2dbcpostgres.vo

import com.toy.webfluxr2dbcpostgres.domain.User
import javax.validation.constraints.NotBlank

data class UserSaveRequestVO(
  @field:NotBlank
  val name: String,
  @field:NotBlank
  val username: String,
  @field:NotBlank
  val password: String
) {
  fun toEntity() = User.newUser(
    name = name,
    username = username,
    password = password
  )
}

data class UserSaveResponseVO(
  val id: Long,
  val name: String,
  val username: String,
) {
  companion object {
    fun of(user: User) = UserSaveResponseVO(
      id = user.id!!,
      name = user.name,
      username = user.username
    )
  }
}

data class UserUpdateRequestVO(
  val name: String? = null,
  val username: String? = null,
  val password: String? = null
)

data class LoginRequestVO(
  val username: String,
  val password: String
)

data class LoginResponseVO(
  val token: String
)