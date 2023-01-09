package com.lecture.firstservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FirstServiceApplication

fun main(args: Array<String>) {
  runApplication<FirstServiceApplication>(*args)
}
