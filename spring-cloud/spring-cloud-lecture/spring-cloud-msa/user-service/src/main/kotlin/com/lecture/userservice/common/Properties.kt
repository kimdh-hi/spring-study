package com.lecture.userservice.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "greeting")
data class GreetingProperties(
  val message: String
)