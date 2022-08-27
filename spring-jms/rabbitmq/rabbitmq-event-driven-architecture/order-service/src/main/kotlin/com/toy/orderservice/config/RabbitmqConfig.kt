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
  @Value("\${rabbitmq.exchange.name}") val exchange: String,

  @Value("\${rabbitmq.queue.order.name}") val orderQueue: String,
  @Value("\${rabbitmq.binding.routing.key}") val orderRoutingKey: String,

  @Value("\${rabbitmq.queue.email.name}") val emailQueue: String,
  @Value("\${rabbitmq.binding.email.routing.key}") val emailRoutingKey: String,
) {

  @Bean
  fun topicExchange(): Exchange = TopicExchange(exchange)

  @Bean
  fun orderQueue(): Queue = Queue(orderQueue)

  @Bean
  fun emailQueue(): Queue = Queue(emailQueue)

  @Bean
  fun orderBinding(): Binding {
    return BindingBuilder.bind(orderQueue())
      .to(topicExchange())
      .with(orderRoutingKey)
      .noargs()
  }

  @Bean
  fun emailBinding(): Binding {
    return BindingBuilder.bind(orderQueue())
      .to(topicExchange())
      .with(emailRoutingKey)
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