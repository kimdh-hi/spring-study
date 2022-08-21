package com.toy.producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.domain.DummyMessage
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class DummyPrefetchProducer(
  private val rabbitTemplate: RabbitTemplate,
  private val objectMapper: ObjectMapper
) {

  fun sendMessage() {
    for (i in 0 until 500) {
      val dummyMessage = DummyMessage("dummy$i", i)
      rabbitTemplate.convertAndSend("x.dummy", "", dummyMessage)
    }
  }
}