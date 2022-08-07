package com.toy.consumer.consumer.`03-exchange`.`04-header`

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.consumer.domain.Furniture
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class HeaderConsumer(
  private val objectMapper: ObjectMapper
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @RabbitListener(queues = ["q.promotion.discount"])
  fun discountListener(message: String) {
    val furniture = objectMapper.readValue(message, Furniture::class.java)
    log.info("discount-listener furniture: $furniture")
  }

  @RabbitListener(queues = ["q.promotion.free-delivery"])
  fun freeDeliveryListener(message: String) {
    val furniture = objectMapper.readValue(message, Furniture::class.java)
    log.info("freeDelivery-listener furniture: $furniture")
  }
}