package com.toy.rabbitmqservice.producer

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class RabbitmqProducer(
  @Value("\${rabbitmq.exchange.name}")
  private val exchangeName: String,

  @Value("\${rabbitmq.routing.key}")
  private val routingKey: String,

  private val rabbitTemplate: RabbitTemplate
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun sendMessage(message: String) {
    log.info("send: {}", message)
    rabbitTemplate.convertAndSend(exchangeName, routingKey, message)
  }
}