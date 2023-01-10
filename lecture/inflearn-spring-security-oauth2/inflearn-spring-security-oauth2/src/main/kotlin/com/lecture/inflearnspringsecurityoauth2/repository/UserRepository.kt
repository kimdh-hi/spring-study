package com.lecture.inflearnspringsecurityoauth2.repository

import com.lecture.inflearnspringsecurityoauth2.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String> {

  fun findByUsername(username: String): User?
}