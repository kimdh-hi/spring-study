package com.lecture.userservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.factory.PasswordEncoderFactories

@Configuration
class PasswordEncoderConfig {
  @Bean
  fun passwordEncoder() = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}