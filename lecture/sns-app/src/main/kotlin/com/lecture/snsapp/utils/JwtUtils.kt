package com.lecture.snsapp.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.*
import javax.crypto.SecretKey

object JwtUtils {

  fun generateToken(
    username: String,
    key: String,
    expiredTimeMs: Long
  ): String {
    val claims = Jwts.claims()
    claims["username"] = username

    return Jwts.builder()
      .addClaims(claims)
      .setIssuedAt(Date(System.currentTimeMillis()))
      .setExpiration(Date(System.currentTimeMillis() + expiredTimeMs))
      .signWith(getKey(key), SignatureAlgorithm.HS256)
      .compact()
  }

  private fun getKey(key: String): Key {
    val keyBytes = key.toByteArray()
    return Keys.hmacShaKeyFor(keyBytes)
  }

  private fun isValid(token: String) {

  }

  fun isExpired(token: String, key: String): Boolean {
    val expiration = extractClaims(token, key).expiration
    return expiration.before(Date())
  }

  fun getUsername(token: String, key: String): String
    = extractClaims(token, key).get("username", String::class.java)

  private fun extractClaims(token: String, key: String): Claims {
    return Jwts.parserBuilder()
      .setSigningKey(getKey(key))
      .build()
      .parseClaimsJwt(token)
      .body
  }
}