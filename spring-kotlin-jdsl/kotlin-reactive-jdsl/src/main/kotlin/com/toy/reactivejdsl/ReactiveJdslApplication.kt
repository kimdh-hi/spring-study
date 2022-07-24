package com.toy.reactivejdsl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveJdslApplication

fun main(args: Array<String>) {
  runApplication<ReactiveJdslApplication>(*args)
}
