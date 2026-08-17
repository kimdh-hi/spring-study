package com.toy.springcacheex.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan
class PropertiesConfig

@ConfigurationProperties(prefix = "spring.data.redis")
data class RedisProperties(
  val host: String,
  val port: Int
)