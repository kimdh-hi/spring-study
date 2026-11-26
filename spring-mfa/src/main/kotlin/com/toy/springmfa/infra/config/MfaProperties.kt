package com.toy.springmfa.infra.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("mfa")
data class MfaProperties(
  val jwt: Jwt,
  val webauthn: WebAuthn,
) {

  data class Jwt(
    val issuer: String,
    val accessTokenTtl: Duration,
    val pendingTokenTtl: Duration,
  )

  data class WebAuthn(
    val rpId: String,
    val rpName: String,
    val allowedOrigins: Set<String>,
  )
}
