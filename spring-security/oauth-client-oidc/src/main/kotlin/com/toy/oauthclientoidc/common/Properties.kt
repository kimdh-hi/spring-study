package com.toy.oauthclientoidc.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "spring.security.oauth2.client")
@ConstructorBinding
data class Oauth2Properties(
  val registration: Map<Idp, Oauth2Resource>
)

enum class Idp {
  GOOGLE,
  NAVER
}

data class Oauth2Resource(
  val clientName: String,
  val clientId: String,
  val clientSecret: String
)

@ConstructorBinding
@ConfigurationProperties(prefix = "custom")
data class CustomProperties(
  val maxByteCount: Int,
  val readWriteTimeoutSeconds: Int,
  val responseTimeoutSeconds: Long,
  val connectTimeoutMillis: Int,
  val maxIdleAndLifeTimeSeconds: Long
)
