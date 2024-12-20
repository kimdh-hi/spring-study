package com.toy.springjacoco.service

import com.toy.springjacoco.domain.User
import com.toy.springjacoco.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
  private val userRepository: UserRepository,
) {

  @Transactional
  fun create(name: String): User {
    return userRepository.save(User(name = name))
  }
}
