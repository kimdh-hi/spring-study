package com.toy.limitsservice.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "limits-service")
data class CustomProperties(
    val maximun: Int,
    val minimum: Int
)
