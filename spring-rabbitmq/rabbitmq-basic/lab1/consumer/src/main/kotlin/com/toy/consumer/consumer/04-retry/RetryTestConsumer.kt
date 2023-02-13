package com.toy.consumer.consumer.`04-retry`

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class RetryTestConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

  @RabbitListener(queues = ["retry-test-q"])
  fun listener(message: String) {
    throw RuntimeException("error...")
    log.info(message)
  }
}