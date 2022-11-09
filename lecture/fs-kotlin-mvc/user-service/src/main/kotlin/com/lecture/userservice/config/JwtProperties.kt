package com.lecture.userservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "jwt")
@ConstructorBinding
data class JwtProperties(
  val issuer: String,
  val subject: String,
  val expiresHours: Long,
  val secret: String
)