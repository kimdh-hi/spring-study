package com.toy.asynccaching

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AsyncCachingApplication

fun main(args: Array<String>) {
  runApplication<AsyncCachingApplication>(*args)
}
