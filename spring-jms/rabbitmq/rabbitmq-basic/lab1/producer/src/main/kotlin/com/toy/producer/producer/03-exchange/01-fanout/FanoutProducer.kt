package com.toy.producer.producer.`03-exchange`.`01-fanout`

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.producer.domain.User
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

/*
x.hr
- q.hr.marketing
- q.hr.rnd
 */
@Service
class FanoutProducer(
  private val rabbitTemplate: RabbitTemplate,
  private val objectMapper: ObjectMapper
) {

  fun sendMessage(user: User) {
    val json = objectMapper.writeValueAsString(user)
    rabbitTemplate.convertAndSend("x.hr", "", json)
  }

}