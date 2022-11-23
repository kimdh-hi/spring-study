package com.toy.springretry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringRetryApplication

fun main(args: Array<String>) {
  runApplication<SpringRetryApplication>(*args)
}
