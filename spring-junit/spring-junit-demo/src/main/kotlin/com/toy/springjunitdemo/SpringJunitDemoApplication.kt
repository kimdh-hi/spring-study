package com.toy.springjunitdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class SpringJunitDemoApplication

fun main(args: Array<String>) {
  runApplication<SpringJunitDemoApplication>(*args)
}
