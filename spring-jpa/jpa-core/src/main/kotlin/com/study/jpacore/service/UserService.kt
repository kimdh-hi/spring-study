package com.study.jpacore.service

import com.study.jpacore.entity.User
import com.study.jpacore.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
  private val userRepository: UserRepository,
) {

  @Transactional
  fun save(name: String): User {
    return userRepository.save(User.of(name))
  }

  @Transactional(readOnly = true)
  fun getList(): List<User> {
    return userRepository.findAll()
  }
}
