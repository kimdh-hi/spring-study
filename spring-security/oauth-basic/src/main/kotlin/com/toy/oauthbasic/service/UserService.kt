package com.toy.oauthbasic.service

import com.toy.oauthbasic.domain.User
import com.toy.oauthbasic.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository
) {

  fun get(userId: String): User {
    return userRepository.findByIdOrNull(userId) ?: throw RuntimeException("not found")
  }
}