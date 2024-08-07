package com.toy.oauthbasic.repository

import com.toy.oauthbasic.domain.User
import com.toy.oauthbasic.domain.UserConnect
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {
  fun findByUsername(username: String): User?
}

interface UserConnectRepository: CrudRepository<UserConnect, String>