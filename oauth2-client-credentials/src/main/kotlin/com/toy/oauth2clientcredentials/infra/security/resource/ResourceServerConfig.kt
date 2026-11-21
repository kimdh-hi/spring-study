package com.toy.oauth2clientcredentials.infra.security.resource

import com.toy.oauth2clientcredentials.domain.model.ApiScope
import com.toy.oauth2clientcredentials.infra.security.ClientJwtCodec
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
class ResourceServerConfig(
  private val clientJwtCodec: ClientJwtCodec,
) {

  @Bean
  @Order(2)
  fun clientApiFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .securityMatcher("/api/**")
      .authorizeHttpRequests {
        it
          .requestMatchers("/api/service-a/**").hasAuthority(ApiScope.SERVICE_A.authority)
          .requestMatchers("/api/service-b/**").hasAuthority(ApiScope.SERVICE_B.authority)
          .anyRequest().denyAll()
      }
      .oauth2ResourceServer { it.jwt { jwt -> jwt.decoder(clientJwtCodec.decoder()) } }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .csrf { it.disable() }
    return http.build()
  }
}
