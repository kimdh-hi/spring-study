package com.lecture.utils

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
}