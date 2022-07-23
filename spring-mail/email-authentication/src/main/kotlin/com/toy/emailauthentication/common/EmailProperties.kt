package com.toy.emailauthentication.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "email")
data class EmailProperties(val fromAddress: String)