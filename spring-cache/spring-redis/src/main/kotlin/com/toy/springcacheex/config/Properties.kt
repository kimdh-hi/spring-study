package com.toy.springcacheex.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan
class PropertiesConfig

@ConfigurationProperties(prefix = "spring.redis")
data class RedisProperties(
  val host: String,
  val port: Int
)