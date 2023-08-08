package com.study.springsecuritybasic.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

  @GetMapping("/ignoring")
  fun ignoring() = "ignoring"

  @GetMapping("/auth")
  fun auth() = "auth"
}