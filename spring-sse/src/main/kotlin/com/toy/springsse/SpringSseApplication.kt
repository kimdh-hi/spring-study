package com.toy.springsse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class SpringSseApplication

fun main(args: Array<String>) {
  runApplication<SpringSseApplication>(*args)
}
