package com.toy.springboot3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringBoot3Application

fun main(args: Array<String>) {
  runApplication<SpringBoot3Application>(*args)
}
