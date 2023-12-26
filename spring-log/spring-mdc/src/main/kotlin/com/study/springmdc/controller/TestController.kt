package com.study.springmdc.controller

import com.study.springmdc.service.TestService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
  private val testService: TestService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping("/test")
  fun test(): String {
    log.debug("TestController.test")
    testService.service()
    return "ok"
  }
}