package com.study.archunit.user.application

import com.study.archunit.user.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
  private val userRepository: UserRepository,
) {

  @Transactional(readOnly = true)
  fun findAll() = userRepository.findAll()

  @Transactional(readOnly = true)
  fun getOneOrNull() = userRepository.findAll().firstOrNull()
}
