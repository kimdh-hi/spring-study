package com.toy.springdataenvers.repository

import com.toy.springdataenvers.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String>