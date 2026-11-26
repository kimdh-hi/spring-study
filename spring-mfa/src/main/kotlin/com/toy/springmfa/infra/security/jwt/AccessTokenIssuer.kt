package com.toy.springmfa.infra.security.jwt

import com.toy.springmfa.infra.config.MfaProperties
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.UUID
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component

@Component
class AccessTokenIssuer(
  private val jwtEncoder: JwtEncoder,
  private val mfaProperties: MfaProperties,
  private val clock: Clock,
) {

  fun issue(username: String, roles: Collection<String>, factors: Map<String, Instant>, ttl: Duration): String {
    val now = clock.instant()
    val claims = JwtClaimsSet.builder()
      .issuer(mfaProperties.jwt.issuer)
      .subject(username)
      .issuedAt(now)
      .expiresAt(now.plus(ttl))
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
