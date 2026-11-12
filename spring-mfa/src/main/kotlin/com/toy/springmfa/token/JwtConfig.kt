package com.toy.springmfa.token

import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.gen.ECKeyGenerator
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.toy.springmfa.config.MfaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtIssuerValidator
import org.springframework.security.oauth2.jwt.JwtTimestampValidator
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.time.Clock

@Configuration
class JwtConfig {

  private val signingKey: ECKey = ECKeyGenerator(Curve.P_256).keyIDFromThumbprint(true).generate()

  @Bean
  fun jwtEncoder(): JwtEncoder = NimbusJwtEncoder(ImmutableJWKSet(JWKSet(signingKey)))

  @Bean
  fun jwtDecoder(properties: MfaProperties, clock: Clock): JwtDecoder =
    NimbusJwtDecoder.withJwkSource(ImmutableJWKSet(JWKSet(signingKey.toPublicJWK())))
      .jwsAlgorithm(SignatureAlgorithm.ES256)
      .build()
      .apply {
        setJwtValidator(
          DelegatingOAuth2TokenValidator(
            JwtTimestampValidator().apply { setClock(clock) },
            JwtIssuerValidator(properties.jwt.issuer),
          ),
        )
      }
}
