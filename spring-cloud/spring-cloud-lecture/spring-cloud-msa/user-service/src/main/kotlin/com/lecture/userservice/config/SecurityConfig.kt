package com.lecture.userservice.config

import com.lecture.userservice.filter.AuthenticationFilter
import com.lecture.userservice.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
class SecurityConfig(
  private val userService: UserService,
  private val passwordEncoder: PasswordEncoder,
  private val environment: Environment
) {

  @Bean
  fun securityFilterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {

    return http
      .authorizeRequests()
      .antMatchers("/actuator/**").permitAll()
      .antMatchers("/health-check/**").permitAll()
      .antMatchers(HttpMethod.POST, "/users/**").permitAll()
      .antMatchers(HttpMethod.GET, "/users/**").authenticated()
      .and()
      .addFilter(getAuthenticationFilter(authenticationManager))

      .csrf().disable()
      .headers().frameOptions().disable()

      .and()
      .build()
  }

  private fun getAuthenticationFilter(authenticationManager: AuthenticationManager): AuthenticationFilter {
    val authenticationFilter = AuthenticationFilter(userService, environment)
    authenticationFilter.setAuthenticationManager(authenticationManager)

    return authenticationFilter
  }

  @Bean
  fun authenticationManagerBean(http: HttpSecurity): AuthenticationManager {
    val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
    authenticationManagerBuilder
      .userDetailsService<UserDetailsService>(userService)
      .passwordEncoder(passwordEncoder)

    return authenticationManagerBuilder.build()
  }
}