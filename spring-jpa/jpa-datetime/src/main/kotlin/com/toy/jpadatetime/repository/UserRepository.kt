package com.toy.jpadatetime.repository

import com.toy.jpadatetime.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String>