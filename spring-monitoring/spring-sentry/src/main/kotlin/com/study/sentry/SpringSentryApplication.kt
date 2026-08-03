package com.study.sentry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringSentryApplication

fun main(args: Array<String>) {
  runApplication<SpringSentryApplication>(*args)
}
