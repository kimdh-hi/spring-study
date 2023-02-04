package com.toy.springcloudconfigtest

import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController(
  private val env: Environment,
  private val customProperties: CustomProperties
) {

  @GetMapping("/v1")
  fun test1(): String? {
    return env.getProperty("custom.test")
  }

  @GetMapping("/v2")
  fun test2(): String {
    return customProperties.test
  }

  @GetMapping("/enc/v1")
  fun enc1(): String? = env.getProperty("custom.enc")

  @GetMapping("/enc/v2")
  fun enc2(): String = customProperties.enc
}