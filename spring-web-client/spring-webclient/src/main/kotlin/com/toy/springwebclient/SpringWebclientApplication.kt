package com.toy.springwebclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringWebclientApplication

fun main(args: Array<String>) {
  runApplication<SpringWebclientApplication>(*args)
}
