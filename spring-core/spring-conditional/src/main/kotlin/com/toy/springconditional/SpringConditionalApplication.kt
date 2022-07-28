package com.toy.springconditional

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringConditionalApplication

fun main(args: Array<String>) {
	runApplication<SpringConditionalApplication>(*args)
}
