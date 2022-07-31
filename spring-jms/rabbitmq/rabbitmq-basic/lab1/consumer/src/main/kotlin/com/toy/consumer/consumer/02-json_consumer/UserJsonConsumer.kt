package com.toy.consumer.consumer.`02-json_consumer`

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.consumer.domain.User
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class UserJsonConsumer(
  private val objectMapper: ObjectMapper
) {

  private val log = LoggerFactory.getLogger(javaClass)

//  @RabbitListener(queues = ["test.user"])
  fun listener(message: String) {
    val user = objectMapper.readValue(message, User::class.java)
    log.info("employee: {}", user)
  }
}