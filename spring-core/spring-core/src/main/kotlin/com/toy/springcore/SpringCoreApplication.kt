package com.toy.springcore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringCoreApplication

fun main(args: Array<String>) {
  runApplication<SpringCoreApplication>(*args)
}
