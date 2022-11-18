package com.toy.redissondistributedlock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class RedissonDistributedLockApplication

fun main(args: Array<String>) {
  runApplication<RedissonDistributedLockApplication>(*args)
}
