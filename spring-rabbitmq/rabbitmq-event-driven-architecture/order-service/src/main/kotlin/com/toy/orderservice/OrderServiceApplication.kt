package com.toy.orderservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class OrderServiceApplication

fun main(args: Array<String>) {
  runApplication<OrderServiceApplication>(*args)
}
