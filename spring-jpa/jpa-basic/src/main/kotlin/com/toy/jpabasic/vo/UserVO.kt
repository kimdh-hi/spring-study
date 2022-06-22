package com.toy.jpabasic.vo

import com.toy.jpabasic.domain.User

data class UserSaveRequestVO(
  val username: String
) {

  fun toEntity() = User(username = username)
}