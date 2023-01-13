package com.lecture.userservice.config

import com.lecture.userservice.filter.AuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories

@EnableWebSecurity
class SecurityConfig {

  @Bean
  fun securityFilterChain(http: HttpSecurity) = http
    .csrf().disable()
    .headers().frameOptions().disable()
    .and()
    .build()

  @Bean
  fun passwordEncoder() = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}