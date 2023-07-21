package com.toy.springopenfeign

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class SpringOpenfeignApplication

fun main(args: Array<String>) {
  runApplication<SpringOpenfeignApplication>(*args)
}
