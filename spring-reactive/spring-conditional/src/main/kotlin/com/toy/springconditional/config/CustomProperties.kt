package com.toy.springconditional.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "cache")
data class CustomProperties(
  val redis: Boolean?,
  val ehcache: Boolean?,
)