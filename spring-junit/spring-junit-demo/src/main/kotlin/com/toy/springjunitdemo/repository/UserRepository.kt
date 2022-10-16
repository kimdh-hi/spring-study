package com.toy.springjunitdemo.repository

import com.toy.springjunitdemo.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {
}