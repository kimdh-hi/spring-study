package com.toy.dpopproofjwt.application.auth

interface TokenIssuer {
  fun issue(subject: String, jkt: String): AccessToken
}

data class AccessToken(
  val value: String,
  val type: TokenType,
  val expiresIn: Long,
)

data class IssuedToken(
  val accessToken: AccessToken,
  val refreshToken: String,
)

enum class TokenType(val value: String) {
  BEARER("Bearer"),
  DPOP("DPoP"),
}
