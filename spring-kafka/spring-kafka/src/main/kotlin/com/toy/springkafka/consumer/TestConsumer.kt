package com.toy.springkafka.consumer

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TestConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

  @KafkaListener(id = "test-id", topics = ["test-topic"])
  fun listener(message: String) {
    log.info("message: {}", message)
  }
}