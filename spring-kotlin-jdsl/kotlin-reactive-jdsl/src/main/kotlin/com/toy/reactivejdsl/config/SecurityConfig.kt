package com.toy.reactivejdsl.config

import com.toy.reactivejdsl.common.PasswordUtils
import com.toy.reactivejdsl.common.UrlConstants.SWAGGER_V3
import com.toy.reactivejdsl.security.JwtAuthenticationFilter
import com.toy.reactivejdsl.security.JwtUtil
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
  val jwtUtil: JwtUtil
) {

  @Bean
  fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
    http
      .csrf { it.disable() }
      .headers { header ->
        header.frameOptions { it.disable() }
        header.contentTypeOptions { it.disable() }
      }
      .requestCache().disable()

      .authorizeExchange { exchange: AuthorizeExchangeSpec ->
        exchange.pathMatchers(HttpMethod.GET, "/api/test/**", "/version.txt", "/upload/**", *SWAGGER_V3).permitAll()
        PathRequest.toStaticResources().atCommonLocations()
      }
      .authorizeExchange { exchange: AuthorizeExchangeSpec ->
        exchange
          .pathMatchers(HttpMethod.POST, "/api/users/signup").permitAll()
          .pathMatchers("/api/login", *SWAGGER_V3).permitAll()
          .anyExchange().authenticated()
      }
      .logout().disable()
      .httpBasic().disable()
      .formLogin().disable()
      .addFilterAt(JwtAuthenticationFilter(jwtUtil), SecurityWebFiltersOrder.AUTHENTICATION)
      .build()


  @Bean
  fun passwordEncoder(): PasswordEncoder = PasswordUtils.passwordEncoder

}