package com.toy.oauthclientoidc.repository

import com.toy.oauthclientoidc.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {
  fun findByUsername(username: String): User?
}