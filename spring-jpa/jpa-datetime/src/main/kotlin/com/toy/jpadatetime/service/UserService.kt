package com.toy.jpadatetime.service

import com.toy.jpadatetime.controller.UserSaveRequest
import com.toy.jpadatetime.domain.User
import com.toy.jpadatetime.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
  private val userRepository: UserRepository
) {

  fun save(request: UserSaveRequest): User {
    val user = request.toEntity()
    return userRepository.save(user)
  }
}