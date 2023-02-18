package com.toy.jpasecondlevelcache

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaSecondLevelCacheApplication

fun main(args: Array<String>) {
  runApplication<JpaSecondLevelCacheApplication>(*args)
}
