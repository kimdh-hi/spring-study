package com.toy.springconditional.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "cache")
data class CacheProperties(
  val cacheName: String,
)

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp")
data class AmqpProperties(
  val enabled: Boolean
)