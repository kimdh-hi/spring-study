package com.toy.springintegrationdemo.config.activator

import org.slf4j.LoggerFactory
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.stereotype.Component

@Component
class TestServiceActivator {
  private val log = LoggerFactory.getLogger(javaClass)

  @ServiceActivator
  fun testService(message: String) {
    log.info("testService msg: $message")
  }

//  @ServiceActivator
//  fun testService2(message: String) {
//    log.info("testService2 msg: $message")
//  }
}
