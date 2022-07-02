package com.toy.gradletoproperties.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "custom-settings")
@ConstructorBinding
data class CustomProperties(
    val key1: String,
    val key2: String
)