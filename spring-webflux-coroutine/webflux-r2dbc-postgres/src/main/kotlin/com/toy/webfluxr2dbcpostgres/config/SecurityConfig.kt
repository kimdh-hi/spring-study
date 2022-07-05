package com.toy.webfluxr2dbcpostgres.config

import com.toy.webfluxr2dbcpostgres.auth.JwtAuthenticationFilter
import com.toy.webfluxr2dbcpostgres.auth.JwtUtil
import com.toy.webfluxr2dbcpostgres.common.PasswordUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.http
import org.springframework.security.config.web.server.invoke
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
  val jwtUtil: JwtUtil
) {

  // todo, session stateless, cors
  @Bean
  fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
    http {
      csrf { disable() }
      headers {
        frameOptions { disable() }
        contentTypeOptions { disable() }
      }
      authorizeExchange {
        authorize("/api/login", permitAll)
        authorize(anyExchange, authenticated)
      }

      formLogin { disable() }
      httpBasic { disable() }
      logout { disable() }

      addFilterAt(JwtAuthenticationFilter(jwtUtil), SecurityWebFiltersOrder.AUTHENTICATION)
    }

  @Bean
  fun passwordEncoder(): PasswordEncoder = PasswordUtils.passwordEncoder

//  @Bean
//  fun authenticationWebFilter(
//    authenticationManager: ReactiveAuthenticationManager,
//    jwtConverter: ServerAuthenticationConverter) {
//    val authenticationWebFilter = AuthenticationWebFilter(authenticationManager)
//    authenticationWebFilter.apply {
//      setRequiresAuthenticationMatcher { ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/login").matches(it) } // login
//      setServerAuthenticationConverter(jwtConverter) // authorization -> token
//
//    }
//  }

//  @Bean
//  fun reactiveAuthenticationManager(): ReactiveAuthenticationManager {
//    return DelegatingReactiveAuthenticationManager()
//  }

}