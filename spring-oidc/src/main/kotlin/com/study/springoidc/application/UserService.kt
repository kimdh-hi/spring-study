package com.study.springoidc.application

import com.study.springoidc.domain.model.User
import com.study.springoidc.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository,
) {

  fun getByUsername(username: String): User =
    userRepository.findByUsername(username) ?: throw NoSuchElementException("user not found: $username")

  @Transactional
  fun createIfAbsent(user: User): User =
    userRepository.findByUsername(user.username) ?: userRepository.save(user)
}
