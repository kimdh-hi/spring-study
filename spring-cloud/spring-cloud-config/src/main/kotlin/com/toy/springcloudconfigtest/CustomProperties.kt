package com.toy.springcloudconfigtest

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "custom")
data class CustomProperties(
  var test: String,
  var enc: String
)