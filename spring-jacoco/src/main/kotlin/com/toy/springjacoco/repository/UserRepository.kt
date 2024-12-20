package com.toy.springjacoco.repository

import com.toy.springjacoco.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
}
