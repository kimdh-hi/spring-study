package com.toy.springstomp.infra.amqp

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
class AmqpPublisher(
  private val rabbitTemplate: RabbitTemplate,
  private val jsonMapper: JsonMapper,
) {
  fun publish(exchange: String, message: Any) =
    rabbitTemplate.convertAndSend(exchange, "", jsonMapper.writeValueAsString(message))
}
