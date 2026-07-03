package com.study.springmodulith

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class SpringModulithApplication

fun main(args: Array<String>) {
  runApplication<SpringModulithApplication>(*args)
}
