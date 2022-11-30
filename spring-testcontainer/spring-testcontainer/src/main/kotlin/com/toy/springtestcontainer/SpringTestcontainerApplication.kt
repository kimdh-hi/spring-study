package com.toy.springtestcontainer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringTestcontainerApplication

fun main(args: Array<String>) {
  runApplication<SpringTestcontainerApplication>(*args)
}
