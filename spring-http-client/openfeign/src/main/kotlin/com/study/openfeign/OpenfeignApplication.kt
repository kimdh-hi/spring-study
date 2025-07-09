package com.study.openfeign

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OpenfeignApplication

fun main(args: Array<String>) {
  runApplication<OpenfeignApplication>(*args)
}
