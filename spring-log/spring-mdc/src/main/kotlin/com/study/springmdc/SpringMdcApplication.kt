package com.study.springmdc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringMdcApplication

fun main(args: Array<String>) {
  runApplication<SpringMdcApplication>(*args)
}
