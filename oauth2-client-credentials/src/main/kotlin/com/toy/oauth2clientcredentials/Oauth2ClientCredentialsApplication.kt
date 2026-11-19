package com.toy.oauth2clientcredentials

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class Oauth2ClientCredentialsApplication

fun main(args: Array<String>) {
  runApplication<Oauth2ClientCredentialsApplication>(*args)
}
