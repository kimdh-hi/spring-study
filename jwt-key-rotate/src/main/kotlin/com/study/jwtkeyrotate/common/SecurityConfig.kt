package com.study.jwtkeyrotate.common

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import com.study.jwtkeyrotate.application.JwtKeyService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(private val keyService: JwtKeyService) {
  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain = http
    .csrf { it.disable() }
    .authorizeHttpRequests { it.requestMatchers("/login", "/refresh").permitAll().anyRequest().authenticated() }
    .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
    .build()

  @Bean
  fun jwtDecoder(): JwtDecoder {
    val processor = DefaultJWTProcessor<SecurityContext>().apply {
      jwsKeySelector = JWSVerificationKeySelector(JWSAlgorithm.HS256) { selector, _ -> selector.select(JWKSet(keyService.getVerificationKeys())) }
    }
    return NimbusJwtDecoder(processor)
  }
}
