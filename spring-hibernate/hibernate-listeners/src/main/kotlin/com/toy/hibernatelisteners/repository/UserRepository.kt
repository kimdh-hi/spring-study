package com.toy.hibernatelisteners.repository

import com.toy.hibernatelisteners.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {
}