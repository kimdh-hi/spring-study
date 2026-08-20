package com.study.springoidc.infra.persistence

import com.study.springoidc.application.UserService
import com.study.springoidc.domain.model.User
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class UserDataInitializer {

  @Bean
  fun userDataLoader(userService: UserService, passwordEncoder: PasswordEncoder) = ApplicationRunner {
    userService.createIfAbsent(
      User(
        username = "user",
        password = passwordEncoder.encode("password")!!,
        email = "user@study.com",
        nickname = "테스트유저",
        roles = "USER",
      )
    )
  }
}
