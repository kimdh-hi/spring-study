package com.toy.rabbitmqservice.config

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import java.io.Serializable

class AmqpSender(
  private val rabbitTemplate: RabbitTemplate,
  private val name: String = ""
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun send(queueName: String, message: Serializable) {
    rabbitTemplate.convertAndSend(queueName, message)
    log.info("AmqpSender [{} -> {}] message: {}", name, queueName, message)
  }
}