package com.toy.springmfa.token

import com.toy.springmfa.config.MfaProperties
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.Instant
import java.util.UUID

@Component
class AccessTokenIssuer(
  private val jwtEncoder: JwtEncoder,
  private val properties: MfaProperties,
  private val clock: Clock,
) {

  fun issue(username: String, roles: Collection<String>, factors: Map<String, Instant>): String {
    val now = clock.instant()
    val claims = JwtClaimsSet.builder()
      .issuer(properties.jwt.issuer)
      .subject(username)
      .issuedAt(now)
      .expiresAt(now.plus(properties.jwt.accessTokenTtl))
      .id(UUID.randomUUID().toString())
      .claim(ROLES_CLAIM, roles)
      .claim(FACTORS_CLAIM, factors.mapValues { it.value.epochSecond })
      .build()
    val header = JwsHeader.with(SignatureAlgorithm.ES256).build()
    return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).tokenValue
  }

  companion object {
    const val ROLES_CLAIM = "roles"
    const val FACTORS_CLAIM = "factors"
  }
}
