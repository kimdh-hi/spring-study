package com.toy.jpafieldencrypt.repository

import com.toy.jpafieldencrypt.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {
  fun findAllByNameContaining(name: String): List<User>

  fun findByName(name: String): User?
}