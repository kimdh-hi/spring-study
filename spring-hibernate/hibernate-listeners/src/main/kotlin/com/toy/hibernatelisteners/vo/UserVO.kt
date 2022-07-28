package com.toy.hibernatelisteners.vo

import com.toy.hibernatelisteners.domain.User

data class UserSaveRequestVO(
  val username: String,
  val password: String
) {
  fun toEntity() = User(username = this.username, password = this.password)
}

data class UserUpdateRequestVO(
  val username: String,
  val password: String
)