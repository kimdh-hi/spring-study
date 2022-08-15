package com.toy.producer.producer

import com.toy.producer.domain.DummyMessage
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class DummyProducer(
  private val rabbitTemplate: RabbitTemplate
) {

  fun sendMessage(message: DummyMessage) {
    rabbitTemplate.convertAndSend("x.dummy", "", message)
  }
}