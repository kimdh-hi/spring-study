package com.toy.springintegrationdemo.config

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
  fun createRequeueTestQueue() = Queue("requeueTestQueue")
}