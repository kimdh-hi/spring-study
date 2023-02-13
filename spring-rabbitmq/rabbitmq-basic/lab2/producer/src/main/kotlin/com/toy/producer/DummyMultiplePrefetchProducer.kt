package com.toy.producer

import com.toy.domain.DummyMessage
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class DummyMultiplePrefetchProducer(private val rabbitTemplate: RabbitTemplate) {

  fun sendLongTimeProcessingMessage() {
    for (i in 0 until 1_000) {
      val dummyMessage = DummyMessage("longTimeProcessingMessage$i", i)
      rabbitTemplate.convertAndSend("x.long-time", "", dummyMessage)
    }
  }

  fun sendShortTimeProcessingMessage() {
    for (i in 0 until 1_000) {
      val dummyMessage = DummyMessage("shortTimeProcessingMessage$i", i)
      rabbitTemplate.convertAndSend("x.short-time", "", dummyMessage)
    }
  }
}