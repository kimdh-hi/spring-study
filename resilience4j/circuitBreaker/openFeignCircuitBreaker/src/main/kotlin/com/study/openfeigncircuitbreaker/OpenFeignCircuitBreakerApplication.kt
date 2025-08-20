package com.study.openfeigncircuitbreaker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OpenFeignCircuitBreakerApplication

fun main(args: Array<String>) {
  runApplication<OpenFeignCircuitBreakerApplication>(*args)
}
