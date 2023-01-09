package com.lecture.userservice.repository

import com.lecture.userservice.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String> {
}