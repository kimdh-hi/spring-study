package com.toy.springexposed.vo

import com.toy.springexposed.domain.User
import org.jetbrains.exposed.sql.ResultRow

data class UserResponsVO(
  val id: String,
  val name: String
) {
  companion object {
    fun of(resultRow: ResultRow) = UserResponsVO(
      id = resultRow[User.userId],
      name = resultRow[User.name]
    )
  }
}