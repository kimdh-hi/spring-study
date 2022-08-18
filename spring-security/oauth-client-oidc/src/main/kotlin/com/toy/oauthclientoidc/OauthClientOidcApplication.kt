package com.toy.oauthclientoidc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class OauthClientOidcApplication

fun main(args: Array<String>) {
  runApplication<OauthClientOidcApplication>(*args)
}
