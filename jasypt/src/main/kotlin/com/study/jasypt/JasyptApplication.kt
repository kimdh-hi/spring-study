package com.study.jasypt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class JasyptApplication

fun main(args: Array<String>) {
	runApplication<JasyptApplication>(*args)
}
