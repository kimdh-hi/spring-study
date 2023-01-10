package com.toy.jpaeventlistener

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaEventListenerApplication

fun main(args: Array<String>) {
  runApplication<JpaEventListenerApplication>(*args)
}
