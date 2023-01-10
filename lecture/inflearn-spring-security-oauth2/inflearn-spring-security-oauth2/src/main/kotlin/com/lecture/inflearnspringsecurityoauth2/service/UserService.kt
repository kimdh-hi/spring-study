package com.lecture.inflearnspringsecurityoauth2.service

import com.lecture.inflearnspringsecurityoauth2.domain.User
import com.lecture.inflearnspringsecurityoauth2.model.ProviderUser
import com.lecture.inflearnspringsecurityoauth2.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository
) {

  @Transactional
  fun save(registrationId: String, providerUser: ProviderUser): User {
    val user = User(
      registrationId = registrationId,
      name = providerUser.getUsername(),
      username = providerUser.getEmail(),
      password = providerUser.getPassword(),
    )
    return userRepository.save(user)
  }
}