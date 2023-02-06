package com.toy.springdataenvers.service

import com.toy.springdataenvers.domain.User
import com.toy.springdataenvers.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
  private val userRepository: UserRepository
) {

  @Transactional
  fun save(name: String): User {
    val user = User(name = name)
    return userRepository.save(user)
  }

  @Transactional
  fun update(id: String, name: String) {
    val user = userRepository.findByIdOrNull(id) ?: throw RuntimeException("...")
    user.name = name
  }

  @Transactional
  fun delete(id: String) {
    val user = userRepository.findByIdOrNull(id) ?: throw RuntimeException("...")
    userRepository.delete(user)
  }
}