package com.toy.producer.producer.`04-retry`

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class RetryTestProducer(
  private val rabbitTemplate: RabbitTemplate
) {

  fun send(message: String) {
    rabbitTemplate.convertAndSend("retry-test-q", message)
  }
}