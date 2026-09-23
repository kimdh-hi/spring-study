package com.toy.springstomp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringStompApplication

fun main(args: Array<String>) {
  runApplication<SpringStompApplication>(*args)
}
