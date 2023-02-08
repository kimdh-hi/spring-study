package com.toy.jpafulltext.service

import com.toy.jpafulltext.domain.User
import com.toy.jpafulltext.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
  private val userRepository: UserRepository
) {

  fun searchByDescriptionV1(description: String): List<User> {
    return userRepository.searchByDescriptionV1(description)
  }

  fun searchByDescriptionV2(description: String): List<User> {
    return userRepository.searchByDescriptionV2(description)
  }
}