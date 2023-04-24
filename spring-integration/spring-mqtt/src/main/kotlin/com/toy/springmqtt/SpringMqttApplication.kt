package com.toy.springmqtt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringMqttApplication

fun main(args: Array<String>) {
  runApplication<SpringMqttApplication>(*args)
}
