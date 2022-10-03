package com.lecture.snsapp.fixture

import com.lecture.snsapp.domain.User

class UserFixture {
  companion object {
    fun get(username: String, password: String): User {
      return User(
        id = 199999L, username = username, password = password
      )
    }
  }
}