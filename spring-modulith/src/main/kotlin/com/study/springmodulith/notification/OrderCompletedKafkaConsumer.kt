package com.study.springmodulith.notification

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Component
class OrderCompletedKafkaConsumer {

  private val logger = LoggerFactory.getLogger(OrderCompletedKafkaConsumer::class.java)

  @KafkaListener(topics = ["order.completed"], groupId = "spring-modulith-notification")
  fun onMessage(
    payload: String,
    @Header(KafkaHeaders.RECEIVED_KEY) orderId: String,
  ) {
    logger.info("Notifying customer about order {}: {}", orderId, payload)
  }
}
