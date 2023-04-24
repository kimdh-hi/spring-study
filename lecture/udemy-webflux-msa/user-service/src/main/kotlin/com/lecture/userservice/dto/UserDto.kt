package com.lecture.userservice.dto

import com.lecture.userservice.entity.User

data class UserDto(
  val id: Int,
  val name: String,
  val balance: Int,
) {
  companion object {
    fun of(user: User) = UserDto(
      id = user.id,
      name = user.name,
      balance = user.balance
    )
  }

  fun toEntity() = User(
    id = id,
    name = name,
    balance = balance
  )
}