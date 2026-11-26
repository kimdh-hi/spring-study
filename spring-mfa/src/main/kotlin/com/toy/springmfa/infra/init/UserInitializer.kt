package com.toy.springmfa.infra.init

import com.toy.springmfa.domain.model.User
import com.toy.springmfa.domain.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserInitializer(
  private val userRepository: UserRepository,
  private val passwordEncoder: PasswordEncoder,
) : ApplicationRunner {

  @Transactional
  override fun run(args: ApplicationArguments) {
    if (userRepository.count() > 0) return
    userRepository.saveAll(
      listOf(
        User("user", passwordEncoder.encode("password")!!, "USER"),
        User("admin", passwordEncoder.encode("password")!!, "USER,ADMIN"),
      ),
    )
  }
}
