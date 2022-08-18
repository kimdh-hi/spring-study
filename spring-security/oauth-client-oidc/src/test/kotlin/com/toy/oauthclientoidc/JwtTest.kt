package com.toy.oauthclientoidc

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.junit.jupiter.api.Test
import java.util.*

class JwtTest {

  @Test
  fun `jjwt`() {
    val token = Jwts.builder()
      .addClaims(
        mapOf("key1" to "value1", "key2" to "value2")
      )
      .signWith(SignatureAlgorithm.HS256, "secret-key")
      .compact()

    print(token)

    val parsedToken: Jws<Claims> = Jwts.parser().setSigningKey("secret-key").parseClaimsJws(token)
    println(parsedToken)
  }

  @Test
  fun `java-jwt`() {
    val token = JWT.create()
      .withClaim("key1", "value1")
      .withClaim("key2", "value2")
      .sign(Algorithm.HMAC256("secret-key"))
    print(token)

    val verifiedToken = JWT
      .require(Algorithm.HMAC256("secret-key"))
      .build()
      .verify(token)
    println(verifiedToken.claims)
  }

  private fun print(token: String) {
    val splitToken = token.split(".")
    println("header: ${String(Base64.getDecoder().decode(splitToken[0]))}")
    println("body: ${String(Base64.getDecoder().decode(splitToken[1]))}")
  }

}