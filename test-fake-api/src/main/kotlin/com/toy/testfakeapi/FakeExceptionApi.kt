package com.toy.testfakeapi

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException

@RestController
@RequestMapping("/fake/exception")
class FakeExceptionApi {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping
  fun exception(): String {
    log.info("/fake/exception - exception called")
    throw IOException("failed...")

    return "ok"
  }
}