package com.toy.springintegrationdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringIntegrationDemoApplication

fun main(args: Array<String>) {
  runApplication<SpringIntegrationDemoApplication>(*args)
}
