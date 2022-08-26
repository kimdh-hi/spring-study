package com.toy.orderservice.config

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitmqConfig(
  @Value("\${rabbitmq.queue.order.name}") val queue: String,
  @Value("\${rabbitmq.exchange.name}") val exchange: String,
  @Value("\${rabbitmq.binding.routing.key}") val routingKey: String,
) {

  @Bean
  fun orderQueue(): Queue {
    return Queue(queue)
  }

  @Bean
  fun topicExchange(): Exchange {
    return TopicExchange(exchange)
  }

  @Bean
  fun bindingBuilder(): Binding {
    return BindingBuilder.bind(orderQueue())
      .to(topicExchange())
      .with(routingKey)
      .noargs()
  }

  @Bean
  fun messageConverter() = Jackson2JsonMessageConverter()

  @Bean
  fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
    val rabbitTemplate = RabbitTemplate(connectionFactory)
    rabbitTemplate.messageConverter = messageConverter()
    return rabbitTemplate
  }

}