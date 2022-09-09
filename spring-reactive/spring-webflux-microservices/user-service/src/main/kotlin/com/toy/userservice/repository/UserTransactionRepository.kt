package com.toy.userservice.repository

import com.toy.userservice.domain.UserTransaction
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface UserTransactionRepository: ReactiveCrudRepository<UserTransaction, Int> {

  fun findByUserId(userId: Int): Flux<UserTransaction>
}