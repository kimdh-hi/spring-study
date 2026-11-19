package com.toy.oauth2clientcredentials.infra.security

import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class AdminTokenIssuer(
  private val adminJwtCodec: AdminJwtCodec,
  private val properties: SecurityProperties,
) {

  fun issue(username: String): IssuedToken {
    val now = Instant.now()
    val claims = JwtClaimsSet.builder()
      .issuer(adminJwtCodec.issuer)
      .subject(username)
      .issuedAt(now)
      .expiresAt(now.plus(properties.adminTokenTtl))
      .claim(ROLES_CLAIM, listOf("ADMIN"))
      .build()
    val header = JwsHeader.with(SignatureAlgorithm.RS256).build()
    val token = adminJwtCodec.encoder.encode(JwtEncoderParameters.from(header, claims)).tokenValue
    return IssuedToken(token, properties.adminTokenTtl.seconds)
  }

  companion object {
    const val ROLES_CLAIM = "roles"
  }
}

data class IssuedToken(
  val accessToken: String,
  val expiresIn: Long,
  val tokenType: String = "Bearer",
)
