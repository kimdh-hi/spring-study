package com.toy.springsecuritycors.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@Configuration
@EnableWebSecurity
class SecurityConfig {

  @Bean
  fun configure(http: HttpSecurity) = http
    .cors { it.disable() }
//    .cors {  }
    .build()

}