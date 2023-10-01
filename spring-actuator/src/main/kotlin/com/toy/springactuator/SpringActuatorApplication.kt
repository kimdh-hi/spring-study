package com.toy.springactuator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringActuatorApplication

fun main(args: Array<String>) {
  runApplication<SpringActuatorApplication>(*args)
}
