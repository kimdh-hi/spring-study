package com.study.jwtkeyrotate.common

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("jwt")
data class JwtProperties(
  val ttl: Duration,
  val refreshTtl: Duration,
  val rotationCron: String,
)
