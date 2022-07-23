package com.toy.emailauthentication

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class EmailAuthenticationApplication

fun main(args: Array<String>) {
  runApplication<EmailAuthenticationApplication>(*args)
}
