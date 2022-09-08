package com.toy.springxssprotection.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

  @Bean
  fun configure(http: HttpSecurity): SecurityFilterChain {
    return http
      .csrf().disable()
      .headers { it
        .xssProtection()
        .and()
        .contentSecurityPolicy("script-src 'self'")
      }
      .authorizeHttpRequests().anyRequest().permitAll()
      .and()
      .build()
  }
}