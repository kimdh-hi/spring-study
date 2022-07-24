package com.toy.kotlinjdsl.service

import com.toy.kotlinjdsl.domain.User
import com.toy.kotlinjdsl.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository
) {

  fun get(id: String): User? {
    return userRepository.get(id) ?: throw RuntimeException("data not found ...")
  }

}