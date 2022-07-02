package com.toy.jpabasic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaBasicApplication

fun main(args: Array<String>) {
  runApplication<JpaBasicApplication>(*args)
}
