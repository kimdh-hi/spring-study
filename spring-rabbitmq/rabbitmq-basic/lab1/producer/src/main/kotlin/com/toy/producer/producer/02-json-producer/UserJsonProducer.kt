package com.toy.producer.producer.`02-json-producer`

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.producer.domain.User
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class UserJsonProducer(
  private val rabbitTemplate: RabbitTemplate,
  private val objectMapper: ObjectMapper
) {
  fun sendMessage(user: User) {
    val json = objectMapper.writeValueAsString(user)
    rabbitTemplate.convertAndSend("test.employee", json)
  }
}