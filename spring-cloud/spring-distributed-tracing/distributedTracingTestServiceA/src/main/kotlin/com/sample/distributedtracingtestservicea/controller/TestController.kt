package com.sample.distributedtracingtestservicea.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping("/test")
  fun test(): String {
    log.info("test..")

    return "test"
  }
}