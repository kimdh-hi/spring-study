package com.toy.coroutinevsreactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveApplication

fun main(args: Array<String>) {
  runApplication<ReactiveApplication>(*args)
}