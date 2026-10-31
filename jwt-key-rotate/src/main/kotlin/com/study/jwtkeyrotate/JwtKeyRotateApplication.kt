package com.study.jwtkeyrotate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@ConfigurationPropertiesScan
@SpringBootApplication
class JwtKeyRotateApplication

fun main(args: Array<String>) {
  runApplication<JwtKeyRotateApplication>(*args)
}
