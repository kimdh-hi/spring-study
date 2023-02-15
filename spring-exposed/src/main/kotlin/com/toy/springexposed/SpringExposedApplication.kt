package com.toy.springexposed

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringExposedApplication

fun main(args: Array<String>) {
  runApplication<SpringExposedApplication>(*args)
}
