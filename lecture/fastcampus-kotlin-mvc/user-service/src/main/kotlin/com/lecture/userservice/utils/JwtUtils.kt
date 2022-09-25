package com.lecture.userservice.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.lecture.userservice.config.JwtProperties
import java.util.*

object JwtUtils {

  fun createToken(claims: JwtClaim, jwtProperties: JwtProperties): String {
    return JWT.create()
      .withIssuer(jwtProperties.issuer)
      .withSubject(jwtProperties.subject)
      .withIssuedAt(Date())
      .withExpiresAt(Date(Date().time + jwtProperties.expiresHours))
      .withClaim("userId", claims.userId)
      .withClaim("username", claims.username)
      .withClaim("name", claims.name)
      .withClaim("profileUrl", claims.profileUrl)
      .sign(Algorithm.HMAC256(jwtProperties.secret))
  }

  fun decode(token: String, secret: String, issuer: String): DecodedJWT {
    val algorithm = Algorithm.HMAC256(secret)
    val verifier = JWT.require(algorithm)
      .withIssuer(issuer)
      .build()
    return verifier.verify(token)
  }
}

data class JwtClaim(
  val userId: Long,
  val username: String,
  val name: String,
  val profileUrl: String
)