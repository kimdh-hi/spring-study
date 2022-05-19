package com.study.jwt.repository

import com.study.jwt.domain.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository: JpaRepository<Account, String> {

    fun findByUsername(username: String): Account?
    fun existsByUsername(username: String): Boolean
}