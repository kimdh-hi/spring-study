package com.toy.emailservice.config

import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitmqConfig {

  @Bean
  fun messageConverter() = Jackson2JsonMessageConverter()

  @Bean
  fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
    val rabbitTemplate = RabbitTemplate(connectionFactory)
    rabbitTemplate.messageConverter = messageConverter()
    return rabbitTemplate
  }

}