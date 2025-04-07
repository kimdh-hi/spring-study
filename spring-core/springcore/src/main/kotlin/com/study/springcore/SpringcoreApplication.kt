package com.study.springcore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringcoreApplication

fun main(args: Array<String>) {
  runApplication<SpringcoreApplication>(*args)
}
