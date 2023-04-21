package com.lecture.userservice.repository

import com.lecture.userservice.entity.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface UserRepository: ReactiveCrudRepository<User, Int> {
}