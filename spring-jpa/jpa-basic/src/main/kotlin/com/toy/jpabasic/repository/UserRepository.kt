package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String>