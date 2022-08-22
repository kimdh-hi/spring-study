package com.toy.oauthbasic.oauth2

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "spring.security.oauth2.client")
@ConstructorBinding
data class OAuth2Properties(
  val registration: Map<Idp, OAuth2Resource>
)

data class OAuth2Resource(
  val clientName: String,
  val clientId: String,
  val clientSecret: String
)