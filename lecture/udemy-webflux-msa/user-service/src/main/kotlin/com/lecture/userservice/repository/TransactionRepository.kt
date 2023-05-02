package com.lecture.userservice.repository

import com.lecture.userservice.entity.UserTransaction
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface TransactionRepository: ReactiveCrudRepository<UserTransaction, Int> {

  fun findByUserId(userId: Int): Flux<UserTransaction>
}