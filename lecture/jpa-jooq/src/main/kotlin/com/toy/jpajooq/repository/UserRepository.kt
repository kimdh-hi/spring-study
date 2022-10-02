package com.toy.jpajooq.repository

import com.toy.jpajooq.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {
}