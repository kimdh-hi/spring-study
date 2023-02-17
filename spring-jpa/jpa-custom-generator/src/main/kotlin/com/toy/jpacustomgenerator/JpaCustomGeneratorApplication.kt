package com.toy.jpacustomgenerator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaCustomGeneratorApplication

fun main(args: Array<String>) {
  runApplication<JpaCustomGeneratorApplication>(*args)
}
