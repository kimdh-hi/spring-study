package com.lecture.divelog.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "site")
@ConstructorBinding
data class SiteProperties(
  val author: String,
  val email: String
)