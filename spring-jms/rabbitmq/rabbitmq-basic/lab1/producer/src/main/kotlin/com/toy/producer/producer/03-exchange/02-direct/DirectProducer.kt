package com.toy.producer.producer.`03-exchange`.`02-direct`

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.producer.domain.User
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

/*
x.user
- ADMIN: q.user.admin
- USER: q.user.user

routingKey 로 바인딩 된 큐를 식별하여 전달
 */
@Service
class DirectProducer(
  private val rabbitTemplate: RabbitTemplate,
  private val objectMapper: ObjectMapper
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun sendMessage(user: User) {
    log.info("send: {}", user)
    val json = objectMapper.writeValueAsString(user)
    rabbitTemplate.convertAndSend("x.user", user.role!!.name, json)
  }

}