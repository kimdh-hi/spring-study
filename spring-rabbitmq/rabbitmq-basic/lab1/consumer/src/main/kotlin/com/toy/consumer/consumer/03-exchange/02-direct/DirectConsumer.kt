package com.toy.consumer.consumer.`03-exchange`.`02-direct`

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.consumer.domain.User
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

//@Service
class DirectConsumer(
  private val objectMapper: ObjectMapper
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @RabbitListener(queues = ["q.user.user"])
  fun userListener(message: String) {
    val user = objectMapper.readValue(message, User::class.java)
    log.info("user.user: {}", user)
  }

  @RabbitListener(queues = ["q.user.admin"])
  fun adminListener(message: String) {
    val admin = objectMapper.readValue(message, User::class.java)
    log.info("user.admin: {}", admin)
  }
}