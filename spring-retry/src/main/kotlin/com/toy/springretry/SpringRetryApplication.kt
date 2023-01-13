package com.toy.springretry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
class SpringRetryApplication

fun main(args: Array<String>) {
  runApplication<SpringRetryApplication>(*args)
}
