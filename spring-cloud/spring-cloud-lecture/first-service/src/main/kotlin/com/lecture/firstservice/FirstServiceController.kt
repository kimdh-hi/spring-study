package com.lecture.firstservice

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/first-service")
class FirstServiceController {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping
  fun welcome() = "welcome first-service"

  @GetMapping("/message")
  fun message(@RequestHeader("first-req-header-key") header: String): String {
    log.info("first-service message: {}", header)
    return "first-service message"
  }

  @GetMapping("/check")
  fun check() = "first-service check"
}