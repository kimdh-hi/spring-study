package com.toy.springkafka.config.handler

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TestHandler {

  private val log = LoggerFactory.getLogger(javaClass)

  fun handle(message: TestTopicMessage) {
    log.info("TestHandler.handle: {}", message)
    if (message.exception) {
      throw IllegalArgumentException("TestHandler.handle error message: $message")
    }
  }
}

data class TestTopicMessage(
  val message: String,
  val exception: Boolean,
)
