package com.toy.webfluxr2dbcpostgres.repository

import com.toy.webfluxr2dbcpostgres.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository: CoroutineCrudRepository<User, Long> {
}