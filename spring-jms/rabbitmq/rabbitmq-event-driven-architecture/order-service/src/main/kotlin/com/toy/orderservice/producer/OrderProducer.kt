package com.toy.orderservice.producer

import com.toy.orderservice.vo.OrderEvent
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class OrderProducer(
  @Value("\${rabbitmq.exchange.name}") val exchange: String,
  @Value("\${rabbitmq.binding.routing.key}") val routingKey: String,

  private val rabbitTemplate: RabbitTemplate
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun sendMessage(orderEvent: OrderEvent) {
    log.info("send orderEvent: {}", orderEvent)
    rabbitTemplate.convertAndSend(exchange, routingKey, orderEvent)
  }
}