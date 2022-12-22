package com.toy.springredischat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringRedisChatApplication

fun main(args: Array<String>) {
  runApplication<SpringRedisChatApplication>(*args)
}
