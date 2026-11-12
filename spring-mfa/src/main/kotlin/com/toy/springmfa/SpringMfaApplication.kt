package com.toy.springmfa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringMfaApplication

fun main(args: Array<String>) {
  runApplication<SpringMfaApplication>(*args)
}
