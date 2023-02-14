package com.toy.springmongo.vo

import com.toy.springmongo.domain.User

class UserRequestVO(
  val name: String,
) {
  fun toEntity() = User(
    name = name
  )
}

class UserResponseVO(
  val id: String,
  val name: String
) {
  companion object {
    fun of(user: User) = UserResponseVO(
      id = user.id!!,
      name = user.name
    )
  }
}