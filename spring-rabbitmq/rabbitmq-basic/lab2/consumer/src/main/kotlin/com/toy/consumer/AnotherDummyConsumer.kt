package com.toy.consumer

import com.toy.domain.DummyMessage
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class AnotherDummyConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

  /*
  대상 exchange, queue 등이 없는 경우 생성
   */
  @RabbitListener(
    bindings = [QueueBinding(
      value = Queue("q.auto-dummy", durable = "true"),
      exchange = Exchange(name = "x.auto-dummy", type = ExchangeTypes.DIRECT, durable = "true"),
      key = ["routing-key"],
      ignoreDeclarationExceptions = "true"
    )])
  fun consume(message: DummyMessage) {
    log.info("message: {}", message)
  }
}