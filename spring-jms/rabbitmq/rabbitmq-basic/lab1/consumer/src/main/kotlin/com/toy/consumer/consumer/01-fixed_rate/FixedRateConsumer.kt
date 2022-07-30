package com.toy.consumer.consumer.`01-fixed_rate`

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

//@Service
class FixedRateConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

  // multi-thread concurrency consuming (multi-consumer...)
  @RabbitListener(queues = ["test.fixrate"], concurrency = "7")
  fun listener(message: String) {
    TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextLong(1000, 2000))
    log.info("{} - Consume message: {}", Thread.currentThread().name, message)
  }
}