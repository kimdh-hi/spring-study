package com.study.springsecuritybasic.repository

import com.study.springsecuritybasic.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {

    fun findByUsername(username: String): User?
}