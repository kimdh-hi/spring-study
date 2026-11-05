package com.toy.dpopproofjwt.domain.repository

import com.toy.dpopproofjwt.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
  fun findByUsername(username: String): User?
}
