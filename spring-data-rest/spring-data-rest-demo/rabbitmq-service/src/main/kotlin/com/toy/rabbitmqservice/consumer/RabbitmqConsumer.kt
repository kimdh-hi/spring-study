package com.toy.rabbitmqservice.consumer

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class RabbitmqConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

  @RabbitListener(queues = ["\${rabbitmq.queue.name}"])
  fun consume(message: String) {
    log.info("consume message: {}", message)
  }
}