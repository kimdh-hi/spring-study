package com.toy.rabbitmqservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RabbitmqServiceApplication

fun main(args: Array<String>) {
  runApplication<RabbitmqServiceApplication>(*args)
}
