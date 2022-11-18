package com.toy.redissondistributedlock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RedissonDistributedLockApplication

fun main(args: Array<String>) {
  runApplication<RedissonDistributedLockApplication>(*args)
}
