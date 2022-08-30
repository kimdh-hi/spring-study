package com.toy.rabbitmqservice.controller

import com.toy.rabbitmqservice.config.AmqpSender
import com.toy.rabbitmqservice.domain.TestMessage
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController(
  private val rabbitTemplate: RabbitTemplate,
) {

  private val amqpSender = AmqpSender(rabbitTemplate)

  @PostMapping
  fun test(): String {
//    rabbitTemplate.convertAndSend("amqp-intg-test-exchange", "", "test")
    amqpSender.send("amqp-intg-test", TestMessage("id-1", "data1"))
    return "ok"
  }
}