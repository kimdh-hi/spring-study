package com.lecture.snsapp.config

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

  @Bean
  fun passwordEncoder() = BCryptPasswordEncoder()

  @Bean
  fun configure(http: HttpSecurity): SecurityFilterChain =
    http
      .csrf { it.disable() }
      .requestMatchers {
        it.requestMatchers(
          PathRequest.toStaticResources().atCommonLocations()
        )
      }
      .authorizeRequests { it
        .antMatchers("/api/*/users/join", "/api/*/users/login").permitAll()
        .antMatchers("/api/**").authenticated()
      }
      .sessionManagement { it
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      }
      .build()
}