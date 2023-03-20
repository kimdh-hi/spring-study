package com.toy.jpajson

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaJsonApplication

fun main(args: Array<String>) {
  runApplication<JpaJsonApplication>(*args)
}
