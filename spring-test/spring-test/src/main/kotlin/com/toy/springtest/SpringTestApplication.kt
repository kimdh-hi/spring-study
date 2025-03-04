package com.toy.springtest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringTestApplication

fun main(args: Array<String>) {
  runApplication<SpringTestApplication>(*args)
}
