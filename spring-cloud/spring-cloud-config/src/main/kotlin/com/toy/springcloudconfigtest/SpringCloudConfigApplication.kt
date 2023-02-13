package com.toy.springcloudconfigtest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.context.config.annotation.RefreshScope

@SpringBootApplication
@ConfigurationPropertiesScan
@RefreshScope
class SpringCloudConfigApplication

fun main(args: Array<String>) {
  runApplication<SpringCloudConfigApplication>(*args)
}
