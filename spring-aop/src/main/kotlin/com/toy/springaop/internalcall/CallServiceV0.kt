package com.toy.springaop.internalcall

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CallServiceV0 {

  private val log = LoggerFactory.getLogger(javaClass)

  fun external() {
    log.info("external...")
    internal()
  }

  fun internal() {
    log.info("internal...")
  }
}