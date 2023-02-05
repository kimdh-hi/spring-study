package com.toy.springcloudconfigtest

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "custom")
class CustomProperties(
  test: String,
  enc: String,
  enc2: String
) {
  var test: String = test
    private set

  var enc: String = enc
    private set

  var enc2: String = enc2
    private set
}