package com.toy.springaop.internalcall

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CallServiceV1 {

  @set:Autowired
  lateinit var callServiceV1: CallServiceV1

  private val log = LoggerFactory.getLogger(javaClass)

  fun external() {
    log.info("external...")
    callServiceV1.internal()
  }

  fun internal() {
    log.info("internal...")
  }
}