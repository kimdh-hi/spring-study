package com.toy.gradletoproperties

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class GradleToPropertiesApplication

fun main(args: Array<String>) {
    runApplication<GradleToPropertiesApplication>(*args)
}
