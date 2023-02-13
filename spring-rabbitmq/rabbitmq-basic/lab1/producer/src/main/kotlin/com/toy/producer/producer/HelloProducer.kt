package com.toy.producer.producer

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class HelloProducer(
  private val rabbitTemplate: RabbitTemplate
) {

  fun send(data: String) {
    rabbitTemplate.convertAndSend("test.hello", data)
  }
}