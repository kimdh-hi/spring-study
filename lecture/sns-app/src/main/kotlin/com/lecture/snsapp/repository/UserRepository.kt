package com.lecture.snsapp.repository

import com.lecture.snsapp.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
  fun findByUsername(username: String): User?
}