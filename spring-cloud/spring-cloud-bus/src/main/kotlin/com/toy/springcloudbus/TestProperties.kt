package com.toy.springcloudbus

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "test")
class TestProperties {
  lateinit var data: String
}

@ConfigurationProperties(prefix = "test2")
class Test2Properties {
  var data: String = ""
}
