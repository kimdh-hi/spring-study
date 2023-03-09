package com.toy.springgradle

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "test")
data class TestProperties(
  val testA: String,
  val testAA: String
)