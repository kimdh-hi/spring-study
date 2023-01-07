package com.lecture.firstservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/first-service")
class FirstServiceController {

  @GetMapping
  fun welcome() = "welcome first-service"

}