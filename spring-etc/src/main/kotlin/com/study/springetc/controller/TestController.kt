package com.study.springetc.controller

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping
  fun test(request: HttpServletRequest) {
    val ip1 = request.remoteAddr
    val ip2 = request.getHeader("x-forwarded-for")
    log.info("ip1: $ip1")
    log.info("ip2: $ip2")
  }
}