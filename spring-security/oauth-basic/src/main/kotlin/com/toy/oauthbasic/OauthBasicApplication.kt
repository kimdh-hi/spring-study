package com.toy.oauthbasic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class OauthBasicApplication

fun main(args: Array<String>) {

  runApplication<OauthBasicApplication>(*args)
}
