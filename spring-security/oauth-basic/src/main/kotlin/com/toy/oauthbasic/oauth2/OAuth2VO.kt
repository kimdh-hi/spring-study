package com.toy.oauthbasic.oauth2

data class JwtResponseVO(
  val token: String
)

data class OAuth2TokenResponseVO(
  val accessToken: String,
  val idToken: String? = null
)