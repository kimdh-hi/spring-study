package com.toy.jpajson.repository

import com.toy.jpajson.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {
}