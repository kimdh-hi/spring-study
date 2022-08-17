package com.toy.springwebclient.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "webclient")
data class WebClientProperties(
  val maxByteCount: Int,
  val readWriteTimeoutSeconds: Int,
  val responseTimeoutSeconds: Long,
  val connectTimeoutMillis: Int,
  val maxIdleAndLifeTimeSeconds: Int
)