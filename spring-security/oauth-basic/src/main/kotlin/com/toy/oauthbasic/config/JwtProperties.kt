package com.toy.oauthbasic.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
  val jwtKey: String,
  val jwtAuthTokenExpiryHours: Long,
)