package com.toy.springintegrationdemo.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitmqConfig {

  @Bean
  fun createTestQueue() = Queue("testQueue")

  @Bean
  fun createLoggingQueue() = Queue("loggingQueue")

  @Bean
  fun createDlx(): Exchange = ExchangeBuilder.fanoutExchange("requeueTestDlx")
    .build()

  @Bean
  fun createRequeueTestQueue() = Queue(
    "requeueTestQueue",
    true, false, false,
    mapOf(
      "x-dead-letter-exchange" to "requeueTestDlx",
    )
  )

  @Bean
  fun createRequeueTestFailQueue() = Queue(
    "requeueTestFailQueue",
    true, false, false,
  )

  @Bean
  fun binding(): Binding = Binding(
    "requeueTestFailQueue", Binding.DestinationType.QUEUE, "requeueTestDlx", "", mapOf()
  )

}