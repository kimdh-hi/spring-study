package com.toy.springaws

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringAwsApplication

fun main(args: Array<String>) {
  runApplication<SpringAwsApplication>(*args)
}
