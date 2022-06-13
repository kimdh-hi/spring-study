package com.toy.coroutinevsreactive.reactive.repository

import com.toy.coroutinevsreactive.domain.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserReactiveRepository : ReactiveCrudRepository<User, Long> {

  fun findByUsername(username: String): Mono<User>
  fun existsByUsername(username: String): Mono<Boolean>

}