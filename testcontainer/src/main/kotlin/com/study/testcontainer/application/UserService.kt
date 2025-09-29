package com.study.testcontainer.application

import com.study.testcontainer.domain.UserRepository
import com.study.testcontainer.ui.UserResponse
import com.study.testcontainer.ui.UserSaveRequest
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
  private val userRepository: UserRepository,
) {

  @Transactional
  fun save(request: UserSaveRequest): String? {
    return userRepository.save(request.toEntity()).id
  }

  @Transactional(readOnly = true)
  @Cacheable(value = ["getUser"])
  fun get(userId: String): UserResponse {
    val user = userRepository.findByIdOrNull(userId) ?: throw IllegalArgumentException("user not found. $userId")
    return UserResponse(user.id!!, user.name)
  }
}
