package com.toy.userservice.repository

import com.toy.userservice.domain.UserTransaction
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface UserTransactionRepository: ReactiveCrudRepository<UserTransaction, Int> {
}