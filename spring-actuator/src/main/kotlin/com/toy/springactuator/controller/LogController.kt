package com.toy.springactuator.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

// /actuator/loggers
// 실시간 로그레벨 변경 sample.http 참고
@RestController
class LogController {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping("/logs")
  fun logs() {
    log.trace("trace log")
    log.debug("debug log")
    log.info("info log")
    log.warn("warn log")
    log.error("error log")
  }
}