package com.toy.oauthclientoidc.auth

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Oauth2TokenRequestVO(
  val code: String,
  val clientId: String,
  val clientSecret: String,
  val redirectUri: String,
  val grantType: String,
  val refreshToken: String? = null
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Oauth2TokenResponseVO(
  val scope: String? = null,
  val tokenType: String,
  val expiresIn: Int,
  val accessToken: String,
  val refreshToken: String? = null,
  val idToken: String? = null
)