package com.toy.testserver

import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController(
  private val env: Environment
) {

  @GetMapping
  fun test(): String? {
    return env["limits-service.minimum"]
  }
}