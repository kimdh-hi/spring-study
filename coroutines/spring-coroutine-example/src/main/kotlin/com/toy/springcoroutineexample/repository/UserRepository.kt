package com.toy.springcoroutineexample.repository

import com.toy.springcoroutineexample.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository: CoroutineCrudRepository<User, Long> {

  suspend fun findByUsername(username: String): User?
}