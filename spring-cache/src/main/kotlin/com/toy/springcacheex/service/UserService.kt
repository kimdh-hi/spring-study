package com.toy.springcacheex.service

import com.toy.springcacheex.domain.User
import com.toy.springcacheex.repository.UserRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(val userRepository: UserRepository) {

  @Cacheable(value = ["user"], key = "#id", unless = "#result == null")
  fun get(id: String): User {
    return userRepository.findByIdOrNull(id)
      ?: throw IllegalArgumentException("not found ...")
  }
}