package com.toy.dpopproofjwt.application.auth

interface TokenIssuer {
  fun issue(subject: String, dPoPProof: String, method: String, targetUri: String): IssuedToken
}

data class IssuedToken(
  val value: String,
  val type: TokenType,
  val expiresIn: Long,
)

enum class TokenType(val value: String) {
  BEARER("Bearer"),
  DPOP("DPoP"),
}
