package com.toy.producer.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.RetryPolicy
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

@Configuration
class RabbitmqConfig {

  @Bean
  fun rabbitTemplate(connectionFactory: CachingConnectionFactory): RabbitTemplate {
    val rabbitTemplate = RabbitTemplate(connectionFactory)
    rabbitTemplate.setRetryTemplate(retryTemplate())
    rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
    return rabbitTemplate
  }

  private fun retryTemplate(): RetryTemplate {
    val retryTemplate = RetryTemplate()
    retryTemplate.setBackOffPolicy(backOffPolicy())
    retryTemplate.setRetryPolicy(retryPolicy())
    return retryTemplate
  }

  private fun backOffPolicy(): ExponentialBackOffPolicy {
    val backOffPolicy = ExponentialBackOffPolicy()
    backOffPolicy.initialInterval = 60_000
    backOffPolicy.multiplier = 2.0
    backOffPolicy.maxInterval = 180_000
    return backOffPolicy
  }

  private fun retryPolicy(): RetryPolicy {
    val retryPolicy = SimpleRetryPolicy()
    retryPolicy.maxAttempts = 3
    return retryPolicy
  }

  @Bean
  fun queue() = Queue("retry-test-q")

  @Bean
  fun objectMapper() = ObjectMapper()
}