package com.study.heapdump

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HeapdumpApplication

fun main(args: Array<String>) {
  runApplication<HeapdumpApplication>(*args)
}
