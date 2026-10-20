package com.toy.warmup

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WarmupApplication

fun main(args: Array<String>) {
  runApplication<WarmupApplication>(*args)
}
