package com.lecture.secondservice

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/second-service")
class SecondServiceController {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping
  fun welcome() = "welcome second-service"

  @GetMapping("/message")
  fun message(@RequestHeader("second-req-header-key") header: String): String {
    log.info("second-service message: {}", header)
    return "second-service message"
  }

  @GetMapping("/check")
  fun check() = "second-service check"
}