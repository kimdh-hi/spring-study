package com.toy.stockservice.consumer

import com.toy.stockservice.vo.OrderEvent
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class OrderConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

  @RabbitListener(queues = ["\${rabbitmq.queue.order.name}"])
  fun consume(orderEvent: OrderEvent) {
    log.info("[OrderConsumer] consume orderEvent: {}", orderEvent)
  }
}