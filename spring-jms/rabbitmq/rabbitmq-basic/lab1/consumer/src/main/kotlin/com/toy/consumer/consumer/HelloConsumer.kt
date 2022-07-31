package com.toy.consumer.consumer

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class HelloConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

//  @RabbitListener(queues = ["test.hello"])
  fun helloConsumer(message: String) {
    log.info(message)
  }
}