package com.toy.dpopproofjwt.infra.security

import com.toy.dpopproofjwt.application.auth.AccessToken
import com.toy.dpopproofjwt.application.auth.TokenIssuer
import com.toy.dpopproofjwt.application.auth.TokenType
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class DPoPJwtTokenIssuer(
  private val jwtEncoder: JwtEncoder,
) : TokenIssuer {

  private val ttl: Duration = Duration.ofMinutes(5)

  override fun issue(subject: String, jkt: String): AccessToken {
    val now = Instant.now()
    val claims = JwtClaimsSet.builder()
      .subject(subject)
      .issuedAt(now)
      .expiresAt(now.plus(ttl))
      .claim("cnf", mapOf("jkt" to jkt))
      .build()

    val jwt = jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(SignatureAlgorithm.ES256).build(), claims))

    return AccessToken(jwt.tokenValue, TokenType.DPOP, ttl.seconds)
  }
}
