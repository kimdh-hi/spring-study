package com.toy.emailservice.consumer

import com.toy.emailservice.vo.OrderEvent
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class EmailConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

  @RabbitListener(queues = ["\${rabbitmq.queue.email.name}"])
  fun consume(orderEvent: OrderEvent) {
    log.info("[EmailConsumer] consume orderEvent: {}", orderEvent)
  }
}