package com.lecture.userservice.repository

import com.lecture.userservice.entity.UserTransaction
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface TransactionRepository: ReactiveCrudRepository<UserTransaction, Int> {
}