package com.toy.springintegration.config.activator

import com.toy.springintegration.config.SampleMessage
import org.slf4j.LoggerFactory
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.stereotype.Component

@Component
class MqttTestHandler {

  private val log = LoggerFactory.getLogger(javaClass)

  @ServiceActivator
  fun execute(message: SampleMessage) {
    log.info("MqttTestHandler [message: {}]", message)
  }

  @ServiceActivator
  fun singleLevelTestExecute(message: SampleMessage) {
    log.info("MqttTestHandler.singleLevelTestExecute [message: {}]", message)
  }

  @ServiceActivator
  fun multiLevelTestExecute(message: SampleMessage) {
    log.info("MqttTestHandler.multiLevelTestExecute [message: {}]", message)
  }
}