package com.toy.springcoroutineexample.service

import com.toy.springcoroutineexample.domain.User
import com.toy.springcoroutineexample.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

  suspend fun findUserByName(name: String): User {
    return userRepository.findByUsername(name)
      ?: throw RuntimeException("user not found ...")
  }
}