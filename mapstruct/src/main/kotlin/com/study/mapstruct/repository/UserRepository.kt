package com.study.mapstruct.repository

import com.study.mapstruct.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String> {
}