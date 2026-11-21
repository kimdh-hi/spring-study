package com.toy.oauth2clientcredentials.infra.security

import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.web.SecurityFilterChain

@Configuration
class AuthorizationServerConfig(
  private val properties: SecurityProperties,
  private val clientJwtCodec: ClientJwtCodec,
) {

  @Bean
  @Order(1)
  fun authorizationServerFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .oauth2AuthorizationServer { http.securityMatcher(it.endpointsMatcher) }
      .authorizeHttpRequests { it.anyRequest().authenticated() }
      .csrf { it.disable() }
    return http.build()
  }

  @Bean
  fun authorizationServerSettings(): AuthorizationServerSettings =
    AuthorizationServerSettings.builder().issuer(properties.issuer).build()

  @Bean
  fun jwkSource(): JWKSource<SecurityContext> = clientJwtCodec.jwkSource
}
