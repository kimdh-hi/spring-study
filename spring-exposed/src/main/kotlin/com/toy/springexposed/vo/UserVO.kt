package com.toy.springexposed.vo

import com.toy.springexposed.domain.User
import org.jetbrains.exposed.sql.ResultRow

data class UserResponseVO(
  val id: String,
  val name: String
) {
  companion object {
    fun of(resultRow: ResultRow) = UserResponseVO(
      id = resultRow[User.id].toString(),
      name = resultRow[User.name]
    )
  }
}