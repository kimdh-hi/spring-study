package com.study.jwtkeyrotate.common

import com.nimbusds.jose.jwk.JWKSet
import com.study.jwtkeyrotate.application.JwtKeyService
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class JwtProvider(
  private val keyService: JwtKeyService,
  private val properties: JwtProperties,
) {
  private val encoder = NimbusJwtEncoder { selector, _ -> selector.select(JWKSet(keyService.getSigningKey())) }

  fun issue(subject: String): String {
    val now = Instant.now()
    val claims = JwtClaimsSet.builder()
      .subject(subject)
      .issuedAt(now)
      .expiresAt(now + properties.ttl)
      .build()
    val header = JwsHeader.with(MacAlgorithm.HS256).build()
    return encoder.encode(JwtEncoderParameters.from(header, claims)).tokenValue
  }
}
