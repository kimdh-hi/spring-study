package com.study.testcontainer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestcontainerApplication

fun main(args: Array<String>) {
  runApplication<TestcontainerApplication>(*args)
}
