package com.toy.springboot3.common

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "custom")
data class CustomProperties(
  val myKey: String
)