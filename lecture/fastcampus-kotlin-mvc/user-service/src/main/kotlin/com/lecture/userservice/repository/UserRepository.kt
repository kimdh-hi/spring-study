package com.lecture.userservice.repository

import com.lecture.userservice.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository: CoroutineCrudRepository<User, Long> {
  suspend fun findByUsername(username: String): User?
}