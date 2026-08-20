package com.study.springoidc.domain.repository

import com.study.springoidc.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
  fun findByUsername(username: String): User?
}
