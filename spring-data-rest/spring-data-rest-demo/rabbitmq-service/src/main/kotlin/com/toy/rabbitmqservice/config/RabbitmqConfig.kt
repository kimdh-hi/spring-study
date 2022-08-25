package com.toy.rabbitmqservice.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitmqConfig(
  @Value("\${rabbitmq.queue.name}")
  private val queueName: String,

  @Value("\${rabbitmq.exchange.name}")
  private val exchangeName: String,

  @Value("\${rabbitmq.routing.key}")
  private val routingKey: String
) {

  @Bean
  fun queue(): Queue {
    return Queue(queueName)
  }

  @Bean
  fun topicExchange(): TopicExchange {
    return TopicExchange(exchangeName)
  }

  @Bean
  fun bindingBuilder(): Binding {
    return BindingBuilder.bind(queue())
      .to(topicExchange())
      .with(routingKey)
  }


}