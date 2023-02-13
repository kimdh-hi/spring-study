package com.toy.consumer.config

import com.fasterxml.jackson.databind.ObjectMapper
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

//  @Bean
  fun rabbitTemplate(connectionFactory: CachingConnectionFactory): RabbitTemplate {
    val rabbitTemplate = RabbitTemplate(connectionFactory)
    rabbitTemplate.setRetryTemplate(retryTemplate())
    return rabbitTemplate
  }

  @Bean
  fun jackson2JsonMessageConverter() = Jackson2JsonMessageConverter()

  private fun retryTemplate(): RetryTemplate {
    val retryTemplate = RetryTemplate()
//    retryTemplate.setBackOffPolicy(backOffPolicy())
//    retryTemplate.setRetryPolicy(retryPolicy())
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
  fun objectMapper() = ObjectMapper()
}