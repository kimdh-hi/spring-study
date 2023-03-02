package com.toy.springneo4j

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringNeo4jApplication

fun main(args: Array<String>) {
  runApplication<SpringNeo4jApplication>(*args)
}
