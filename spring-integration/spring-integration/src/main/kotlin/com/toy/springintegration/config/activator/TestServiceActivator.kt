package com.toy.springintegration.config.activator

import com.toy.springintegration.controller.RouteTestMessage
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

  @ServiceActivator
  fun type1Handler(message: RouteTestMessage) {
    log.info("type1Handler [message: {}]", message)
  }

  @ServiceActivator
  fun type2Handler(message: RouteTestMessage) {
    log.info("type2Handler [message: {}]", message)
  }
}
