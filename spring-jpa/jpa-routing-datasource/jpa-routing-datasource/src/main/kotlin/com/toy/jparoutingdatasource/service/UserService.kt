package com.toy.jparoutingdatasource.service

import com.toy.jparoutingdatasource.domain.User
import com.toy.jparoutingdatasource.repository.UserRepositry
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepositry
) {

  @Transactional
  fun save(user: User) = userRepository.save(user)

  fun get(userId: String) = userRepository.findByIdOrNull(userId) ?: throw RuntimeException("user not found...")
}