package com.toy.springintegrationdemo.config

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.RetryPolicy
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

@Configuration
class RabbitmqConfig {

  @Bean
  fun createTestQueue() = Queue("testQueue")

  @Bean
  fun createLoggingQueue() = Queue("loggingQueue")

  @Bean
  fun createRequeueTestQueue() = Queue("requeueTestQueue")
}