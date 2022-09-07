package com.toy.userservice.dto

import com.toy.userservice.domain.User

data class UserDto(
  val id: Int,
  val name: String,
  val balance: Int
) {
  companion object {
    fun of(user: User) = UserDto(
      id = user.id!!,
      name = user.name,
      balance = user.balance
    )
  }

  fun toEntity() = User(
    name = name,
    balance = balance
  )
}