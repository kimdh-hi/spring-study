package com.toy.embeddedredis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EmbeddedRedisApplication

fun main(args: Array<String>) {
  runApplication<EmbeddedRedisApplication>(*args)
}
