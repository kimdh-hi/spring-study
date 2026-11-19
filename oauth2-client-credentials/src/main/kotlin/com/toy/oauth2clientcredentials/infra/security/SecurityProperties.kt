package com.toy.oauth2clientcredentials.infra.security

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("app.security")
data class SecurityProperties(
  val issuer: String,
  val adminTokenTtl: Duration,
  val clientTokenTtl: Duration,
)
