package com.toy.springcloudconfigtest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringCloudConfigApplication

fun main(args: Array<String>) {
  runApplication<SpringCloudConfigApplication>(*args)
}
