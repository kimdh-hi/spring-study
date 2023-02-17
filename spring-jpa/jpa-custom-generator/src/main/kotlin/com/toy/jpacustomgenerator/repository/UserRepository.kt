package com.toy.jpacustomgenerator.repository

import com.toy.jpacustomgenerator.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String>