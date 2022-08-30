package com.toy.rabbitmqservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "spring.rabbitmq")
@ConstructorBinding
data class RabbitmqProperties(
  val addresses: String,
  val host: String,
  val port: Int,
  val username: String,
  val password: String,
  val connectionTimeout: Int,
  val managementUri: String
)