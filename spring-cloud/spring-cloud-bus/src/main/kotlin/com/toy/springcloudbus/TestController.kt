package com.toy.springcloudbus

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
  private val testProperties: TestProperties,
  private val test2Properties: Test2Properties
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping("/test")
  fun test(): String {
    log.info("testProperties.data=${testProperties.data}")
    log.info("test2Properties.data=${test2Properties.data}")
    return "ok"
  }
}