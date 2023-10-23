package com.toy.springcloudbus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringCloudBusApplication

fun main(args: Array<String>) {
  runApplication<SpringCloudBusApplication>(*args)
}
