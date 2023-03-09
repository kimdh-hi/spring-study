package com.toy.springgradle

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringGradleApplication

fun main(args: Array<String>) {
  runApplication<SpringGradleApplication>(*args)
}
