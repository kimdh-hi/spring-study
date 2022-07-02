package com.toy.coroutinevsreactive.coroutine.vo

import com.toy.coroutinevsreactive.domain.User
import reactor.core.publisher.Mono

data class UserSaveRequestVO (
  var username: String
) {
  fun toEntity(): User {
    val user = User(username = username)
    return user
  }
}

data class UserResponseVO (
  var id: Long,
  var username: String
) {
  companion object {
    fun fromEntity(user: User): UserResponseVO = UserResponseVO(user.id!!, user.username)
    }
}