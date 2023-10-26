package com.toy.springaop.logtrace

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LogTrace {

  private val log = LoggerFactory.getLogger(javaClass)

  fun start() {
    log.info("logTrace start...")
  }

  fun end() {
    log.info("logTrace end...")
  }
}