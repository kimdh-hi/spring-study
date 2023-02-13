package com.toy.springintegration.config.activator

import com.toy.springintegration.controller.RouteTestMessage
import org.slf4j.LoggerFactory
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.stereotype.Component
import java.io.Serial
import java.io.Serializable

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

  @ServiceActivator
  fun splitHandler1(message: SplitTestMessage) {
    log.info("splitHandler1 [message: {}]", message)
  }

  @ServiceActivator
  fun splitHandler2(message: SplitTestMessage) {
    log.info("splitHandler2 [message: {}]", message)
  }
}

data class SplitTestMessage(
  val data: String
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 3564431146572996491L
  }
}
