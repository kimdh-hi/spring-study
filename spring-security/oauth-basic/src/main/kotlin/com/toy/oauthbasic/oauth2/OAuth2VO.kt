package com.toy.oauthbasic.oauth2

data class OAuth2TokenResponseVO(
  val accessToken: String,
  val idToken: String? = null
)