package com.toy.springmongo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringMongoApplication

fun main(args: Array<String>) {
  runApplication<SpringMongoApplication>(*args)
}
