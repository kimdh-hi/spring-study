package com.toy.springmfa.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("mfa")
data class MfaProperties(
  val jwt: Jwt,
  val factorValidDuration: Duration,
  val webauthn: WebAuthn,
) {

  data class Jwt(
    val issuer: String,
    val accessTokenTtl: Duration,
  )

  data class WebAuthn(
    val rpId: String,
    val rpName: String,
    val allowedOrigins: Set<String>,
  )
}
