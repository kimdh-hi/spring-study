package com.study.springoidc.infra.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
class ResourceServerConfig {

  // order 2: /api/** 리소스 서버 체인 (order 1 인가 서버 엔드포인트 다음에 평가)
  @Bean
  @Order(2)
  fun apiFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .securityMatcher("/api/**")
      .authorizeHttpRequests {
        // scope read -> SCOPE_ 접두어 권한으로 매핑됨
        it.requestMatchers("/api/scoped/**").hasAuthority("SCOPE_read")
        it.anyRequest().authenticated()
      }
      // Bearer 토큰 인증이라 세션/CSRF 불필요
      .csrf { it.disable() }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }

    return http.build()
  }

  // order 3: 나머지 전체 요청 폴백 체인 (폼 로그인 / 로그아웃)
  @Bean
  @Order(3)
  fun defaultFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .authorizeHttpRequests {
        it.requestMatchers("/h2-console/**").permitAll()
        it.anyRequest().authenticated()
      }
      .csrf { it.ignoringRequestMatchers("/h2-console/**") }
      // h2-console 은 iframe 사용 -> sameOrigin 허용
      .headers { it.frameOptions { frame -> frame.sameOrigin() } }
      .formLogin(Customizer.withDefaults())
      .logout(Customizer.withDefaults())

    return http.build()
  }
}
