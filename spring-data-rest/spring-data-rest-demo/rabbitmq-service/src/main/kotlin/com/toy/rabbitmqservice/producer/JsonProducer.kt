package com.toy.rabbitmqservice.producer

import com.toy.rabbitmqservice.domain.User
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JsonProducer(
  @Value("\${rabbitmq.exchange.name}")
  private val exchange: String,

  @Value("\${rabbitmq.routing.json.key}")
  private val routingKey: String,

  private val rabbitTemplate: RabbitTemplate
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun sendJsonMessage(user: User) {
    log.info("send json message user: {}", user)
    rabbitTemplate.convertAndSend(exchange, routingKey, user)
  }
}