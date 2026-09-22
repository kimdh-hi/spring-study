package com.study.configclient

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RefreshScope
class TestController(
  @Value("\${test.message}") private val message: String
) {
  @GetMapping("/test")
  fun test() = message
}
