package com.toy.springapiversioning.aop

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "api-version")
data class ApiVersionProperties(
  val prefix: String,
  val versionSymbol: String,
)