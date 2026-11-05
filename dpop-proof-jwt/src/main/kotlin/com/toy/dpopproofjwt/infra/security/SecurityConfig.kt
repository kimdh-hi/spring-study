package com.toy.dpopproofjwt.infra.security

import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.gen.ECKeyGenerator
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain
import java.util.UUID

@Configuration
class SecurityConfig {

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http {
      csrf { disable() }
      sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
      authorizeHttpRequests {
        authorize("/login", permitAll)
        authorize(anyRequest, authenticated)
      }
      oauth2ResourceServer {
        jwt { }
        dPoP { }
      }
    }
    return http.build()
  }

  @Bean
  fun signingKey(): ECKey = ECKeyGenerator(Curve.P_256).keyID(UUID.randomUUID().toString()).generate()

  @Bean
  fun jwtEncoder(signingKey: ECKey): JwtEncoder = NimbusJwtEncoder(ImmutableJWKSet(JWKSet(signingKey)))

  @Bean
  fun jwtDecoder(signingKey: ECKey): JwtDecoder =
    NimbusJwtDecoder.withJwkSource(ImmutableJWKSet(JWKSet(signingKey.toPublicJWK())))
      .jwsAlgorithm(SignatureAlgorithm.ES256)
      .build()

  @Bean
  fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
