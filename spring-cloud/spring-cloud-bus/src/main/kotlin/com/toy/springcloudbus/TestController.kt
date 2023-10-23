package com.toy.springcloudbus

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
  private val testProperties: TestProperties
) {

  @GetMapping("/test")
  fun test() = testProperties.data
}