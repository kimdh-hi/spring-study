package com.toy.springcoroutineexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringCoroutineExampleApplication

fun main(args: Array<String>) {
  runApplication<SpringCoroutineExampleApplication>(*args)
}
