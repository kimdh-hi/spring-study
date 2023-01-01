package com.lecture.cors2

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableWebSecurity
class SecurityConfig {

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http.authorizeRequests().anyRequest().authenticated()
    http.cors().configurationSource(corsConfigurationSource())

    return http.build()
  }

  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource {
    val configuration = CorsConfiguration().apply {
      addAllowedOrigin("*")
//      addAllowedOrigin("http://localhost:8082")
      addAllowedMethod("*")
      addAllowedHeader("*")
//      allowCredentials = true
      maxAge = 3600L
    }

    return UrlBasedCorsConfigurationSource().apply {
      registerCorsConfiguration("/**", configuration)
    }
  }
}