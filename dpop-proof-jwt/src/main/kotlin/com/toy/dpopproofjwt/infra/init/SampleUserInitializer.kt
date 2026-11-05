package com.toy.dpopproofjwt.infra.init

import com.toy.dpopproofjwt.domain.model.User
import com.toy.dpopproofjwt.domain.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class SampleUserInitializer(
  private val userRepository: UserRepository,
  private val passwordEncoder: PasswordEncoder,
) : ApplicationRunner {

  override fun run(args: ApplicationArguments) {
    if (userRepository.findByUsername("user") == null) {
      userRepository.save(User("user", passwordEncoder.encode("password")!!))
    }
  }
}
