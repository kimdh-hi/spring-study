package com.toy.springgraphql.vo

import com.toy.springgraphql.domain.User

data class UserSaveRequestVO(
  val name: String,
  val username: String
)

data class UserResponseVO(
  val id: String?,
  val name: String,
  val username: String
) {
  companion object {
    fun of(user: User) = UserResponseVO(
      id = user.id,
      name = user.name,
      username = user.username
    )
  }
}