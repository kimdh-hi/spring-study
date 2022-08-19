package com.toy.oauthclientoidc.auth

import java.security.Principal

data class JwtPrincipal(
  val userId: String,
  val username: String,
) : Principal {
  override fun getName(): String = username
}