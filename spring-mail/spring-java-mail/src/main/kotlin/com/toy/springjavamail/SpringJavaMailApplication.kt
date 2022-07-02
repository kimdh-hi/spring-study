package com.toy.springjavamail

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringJavaMailApplication

fun main(args: Array<String>) {
    runApplication<SpringJavaMailApplication>(*args)
}
