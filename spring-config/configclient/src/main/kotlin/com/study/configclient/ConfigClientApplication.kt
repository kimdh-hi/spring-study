package com.study.configclient

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class ConfigClientApplication

fun main(args: Array<String>) {
  runApplication<ConfigClientApplication>(*args)
}

@RestController
@RefreshScope
class MessageController(
  @Value("\${message:default}") private val message: String
) {
  @GetMapping("/message")
  fun message() = message
}
