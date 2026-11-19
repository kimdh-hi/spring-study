package com.toy.oauth2clientcredentials.infra.security

import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import com.toy.oauth2clientcredentials.domain.model.ApiScope
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(
  private val properties: SecurityProperties,
  private val clientJwtCodec: ClientJwtCodec,
  private val adminJwtCodec: AdminJwtCodec,
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

  @Bean
  @Order(4)
  fun defaultFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .authorizeHttpRequests {
        it
          .requestMatchers("/error").permitAll()
          .anyRequest().denyAll()
      }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .csrf { it.disable() }
    return http.build()
  }

  @Bean
  fun authorizationServerSettings(): AuthorizationServerSettings =
    AuthorizationServerSettings.builder().issuer(properties.issuer).build()

  @Bean
  fun jwkSource(): JWKSource<SecurityContext> = clientJwtCodec.jwkSource

  @Bean
  fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
