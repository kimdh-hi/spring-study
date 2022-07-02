package com.study.jasypt.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "custom")
data class CustomProperties(
    val plainValue: String,
    val encValue: String)