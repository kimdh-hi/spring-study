package com.toy.emailauthentication.repository

import com.toy.emailauthentication.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {

  fun findByUsername(username: String): User?
}