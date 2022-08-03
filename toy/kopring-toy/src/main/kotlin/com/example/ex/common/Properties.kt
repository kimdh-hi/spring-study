package com.example.ex.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "custom.allow-extension")
data class CustomProperties(
  val imageExtensions: MutableList<String>,
  val fileExtensions: MutableList<String>
)