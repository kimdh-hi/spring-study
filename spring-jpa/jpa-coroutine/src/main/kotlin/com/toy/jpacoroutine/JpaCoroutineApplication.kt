package com.toy.jpacoroutine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaCoroutineApplication

fun main(args: Array<String>) {
  runApplication<JpaCoroutineApplication>(*args)
}
