package com.toy.coroutinevsreactive.coroutine.repository

import com.toy.coroutinevsreactive.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface CoroutineUserRepository : CoroutineCrudRepository<User, Long> {

  suspend fun findByUsername(username: String): User?
  suspend fun existsByUsername(username: String): Boolean

}