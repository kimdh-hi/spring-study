package com.lecture.inflearnspringsecurityoauth2.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http.authorizeRequests().anyRequest().authenticated()
    http.formLogin()
    http.apply(CustomSecurityConfigurer().setSecured(true))

    return http.build()
  }
}