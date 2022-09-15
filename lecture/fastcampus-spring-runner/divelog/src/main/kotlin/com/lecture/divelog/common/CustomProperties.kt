package com.lecture.divelog.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "custom")
@ConstructorBinding
data class CustomProperties(
  val randomValue: String,
  val randomInt: Int,
  val randomLong: Long,
  val randomIntBetween: Int,
  val uuid: String,
)