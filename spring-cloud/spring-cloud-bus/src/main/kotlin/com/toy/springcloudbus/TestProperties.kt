package com.toy.springcloudbus

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "test")
class TestProperties(
  val data: String
)