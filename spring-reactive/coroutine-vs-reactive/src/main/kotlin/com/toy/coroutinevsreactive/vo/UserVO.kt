package com.toy.coroutinevsreactive.vo

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
    fun fromEntity(user: User): Mono<UserResponseVO> {
      return Mono.just(UserResponseVO(user.id!!, user.username))
    }
  }

}