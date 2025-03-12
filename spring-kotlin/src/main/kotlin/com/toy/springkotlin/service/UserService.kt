package com.toy.springkotlin.service

import com.toy.springkotlin.entity.User
import com.toy.springkotlin.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
  private val userRepository: UserRepository,
) {

  @Transactional
  fun save(name: String): String {
    return userRepository.save(User(name = name)).getId()
  }

  @Transactional
  fun update(id: String, name: String) {
    userRepository.findByIdOrNull(id)?.update(name)
  }

  @Transactional
  fun delete(id: String) {
    userRepository.findByIdOrNull(id)?.let { userRepository.delete(it) }
      ?: throw IllegalArgumentException("user not found. userId=$id")
  }
}
