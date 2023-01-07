package com.lecture.secondservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/second-service")
class SecondServiceController {

  @GetMapping
  fun welcome() = "welcome second-service"
}