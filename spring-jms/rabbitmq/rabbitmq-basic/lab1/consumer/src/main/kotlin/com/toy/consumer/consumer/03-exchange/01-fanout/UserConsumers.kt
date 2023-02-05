package com.toy.consumer.consumer.`03-exchange`.`01-fanout`

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.consumer.domain.User
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

/*
x.hr
- q.hr.marketing
- q.hr.rnd
 */
//@Service
class MarketingUserConsumer(
  private val objectMapper: ObjectMapper
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @RabbitListener(queues = ["q.hr.marketing"])
  fun listener(message: String) {
    val user = objectMapper.readValue(message, User::class.java)
    log.info("marketing queue consume: {}", user)
  }
}

//@Service
class RndUserConsumer(
  private val objectMapper: ObjectMapper
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @RabbitListener(queues = ["q.hr.rnd"])
  fun listener(message: String) {
    val user = objectMapper.readValue(message, User::class.java)
    log.info("rnd queue consume: {}", user)
  }
}