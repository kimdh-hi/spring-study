package com.lecture.inflearnspringsecurityoauth2.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {

  @GetMapping("/")
  fun index() = "index"
}