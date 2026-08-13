package com.study.jib

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class JibApplication

fun main(args: Array<String>) {
  runApplication<JibApplication>(*args)
}

@RestController
class HelloController {
  @GetMapping("/hello")
  fun hello() = "hello"
}
