package com.sample.distributedtracingtestservicea

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SleuthZipkinTestAApplication

fun main(args: Array<String>) {
  runApplication<SleuthZipkinTestAApplication>(*args)
}
