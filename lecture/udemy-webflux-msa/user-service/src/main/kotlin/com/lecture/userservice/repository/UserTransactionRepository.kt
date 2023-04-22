package com.lecture.userservice.repository

import com.lecture.userservice.entity.UserTransaction
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface UserTransactionRepository: ReactiveCrudRepository<UserTransaction, Int> {
}