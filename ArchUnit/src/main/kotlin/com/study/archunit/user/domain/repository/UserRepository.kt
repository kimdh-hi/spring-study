package com.study.archunit.user.domain.repository

import com.study.archunit.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
}
