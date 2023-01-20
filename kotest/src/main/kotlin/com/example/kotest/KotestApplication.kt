package com.example.kotest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotestApplication

fun main(args: Array<String>) {
  runApplication<KotestApplication>(*args)
}
