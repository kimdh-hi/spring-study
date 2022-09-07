package com.toy.userservice.repository

import com.toy.userservice.domain.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface UserRepository: ReactiveCrudRepository<User, Int> {
}