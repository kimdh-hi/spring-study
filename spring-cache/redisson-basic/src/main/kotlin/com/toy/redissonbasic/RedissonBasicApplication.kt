package com.toy.redissonbasic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RedissonBasicApplication

fun main(args: Array<String>) {
  runApplication<RedissonBasicApplication>(*args)
}
