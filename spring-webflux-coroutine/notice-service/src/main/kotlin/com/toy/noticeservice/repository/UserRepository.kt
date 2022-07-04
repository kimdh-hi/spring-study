package com.toy.noticeservice.repository

import com.toy.noticeservice.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository: CoroutineCrudRepository<User, Long> {

  suspend fun findByUsername(username: String): User?
}