package com.toy.springcacheex.repository

import com.toy.springcacheex.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String> {
}