package com.toy.redisson

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RedissonApplication

fun main(args: Array<String>) {
  runApplication<RedissonApplication>(*args)
}
