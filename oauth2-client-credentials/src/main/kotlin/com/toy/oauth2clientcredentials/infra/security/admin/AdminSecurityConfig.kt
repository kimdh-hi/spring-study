package com.toy.oauth2clientcredentials.infra.security.admin

import com.toy.oauth2clientcredentials.infra.security.AdminJwtCodec
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
class AdminSecurityConfig(
  private val adminJwtCodec: AdminJwtCodec,
) {

  @Bean
  @Order(3)
  fun adminApiFilterChain(http: HttpSecurity): SecurityFilterChain {
    val converter = JwtAuthenticationConverter().apply {
      setJwtGrantedAuthoritiesConverter(
        JwtGrantedAuthoritiesConverter().apply {
          setAuthoritiesClaimName(AdminTokenIssuer.ROLES_CLAIM)
          setAuthorityPrefix("ROLE_")
        },
      )
    }
    http
      .securityMatcher("/admin/**")
      .authorizeHttpRequests {
        it
          .requestMatchers(HttpMethod.POST, "/admin/login").permitAll()
          .anyRequest().hasRole("ADMIN")
      }
      .oauth2ResourceServer {
        it.jwt { jwt -> jwt.decoder(adminJwtCodec.decoder()).jwtAuthenticationConverter(converter) }
      }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .csrf { it.disable() }
    return http.build()
  }
}
