package com.toy.producer.producer.`01-fixed_rate`

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class FixedRateProducer(
  private val rabbitTemplate: RabbitTemplate
) {
  private val log = LoggerFactory.getLogger(javaClass)
  var i: Int = 0

  // producing message every 500ms
  @Scheduled(fixedRate = 500)
  fun sendMessage() {
    i++
    log.info("send i={}", i)
    rabbitTemplate.convertAndSend("test.fixrate", "Fixed rate: $i")
  }
}