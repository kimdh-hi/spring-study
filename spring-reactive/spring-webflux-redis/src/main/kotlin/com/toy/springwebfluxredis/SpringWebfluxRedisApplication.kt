package com.toy.springwebfluxredis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class SpringWebfluxRedisApplication

fun main(args: Array<String>) {
	runApplication<SpringWebfluxRedisApplication>(*args)
}
