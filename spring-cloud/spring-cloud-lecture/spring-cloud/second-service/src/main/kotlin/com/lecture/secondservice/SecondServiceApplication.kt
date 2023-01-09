package com.lecture.secondservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SecondServiceApplication

fun main(args: Array<String>) {
  runApplication<SecondServiceApplication>(*args)
}
