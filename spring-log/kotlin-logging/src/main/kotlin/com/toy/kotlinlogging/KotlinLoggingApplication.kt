package com.toy.kotlinlogging

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinLoggingApplication

fun main(args: Array<String>) {
  runApplication<KotlinLoggingApplication>(*args)
}
