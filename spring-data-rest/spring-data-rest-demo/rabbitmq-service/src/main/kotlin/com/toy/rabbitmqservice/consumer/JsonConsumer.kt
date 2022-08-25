package com.toy.rabbitmqservice.consumer

import com.toy.rabbitmqservice.domain.User
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JsonConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

  @RabbitListener(queues = ["\${rabbitmq.queue.json.name}"])
  fun consume(user: User) {
    log.info("received message: {}", user)
  }

}