package com.toy.jpafulltext

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaFulltextApplication

fun main(args: Array<String>) {
  runApplication<JpaFulltextApplication>(*args)
}
