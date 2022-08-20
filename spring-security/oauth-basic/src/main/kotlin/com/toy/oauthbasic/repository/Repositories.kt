package com.toy.oauthbasic.repository

import com.toy.oauthbasic.domain.User
import com.toy.oauthbasic.domain.UserOAuth2Login
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {
  fun findByUsername(username: String): User?
}

interface UserOAuth2LoginRepository: CrudRepository<UserOAuth2Login, String>