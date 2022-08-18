package com.toy.oauthclientoidc.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "spring.security.oauth2.client")
@ConstructorBinding
data class Oauth2Properties(
  val registration: Map<Oauth2Client, Oauth2Resource>
)

enum class Oauth2Client {
  GOOGLE,
  NAVER
}

data class Oauth2Resource(
  val clientName: String,
  val clientId: String,
  val clientSecret: String
)