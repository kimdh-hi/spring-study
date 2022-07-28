package com.toy.limitsservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class LimitsServiceApplication

fun main(args: Array<String>) {
    runApplication<LimitsServiceApplication>(*args)
}
