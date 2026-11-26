package com.toy.springmfa.domain.repository

import com.toy.springmfa.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

  fun findByUsername(username: String): User?
}
