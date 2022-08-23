package com.toy.oauthbasic.vo

import com.toy.oauthbasic.domain.User

data class UserMeResponseVO(
  val id: String,
  val name: String,
  val username: String
) {
  companion object {
    fun of(user: User): UserMeResponseVO {
      return UserMeResponseVO(
        id = user.id!!,
        name = user.name, username = user.username
      )
    }
  }
}