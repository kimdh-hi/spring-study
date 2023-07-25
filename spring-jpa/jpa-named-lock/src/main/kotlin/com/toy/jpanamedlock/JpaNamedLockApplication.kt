package com.toy.jpanamedlock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaNamedLockApplication

fun main(args: Array<String>) {
  runApplication<JpaNamedLockApplication>(*args)
}
