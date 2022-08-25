package com.toy.rabbitmqservice.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitmqConfig(
  @Value("\${rabbitmq.queue.name}")
  private val queueName: String,

  @Value("\${rabbitmq.queue.json.name}")
  private val jsonQueueName: String,

  @Value("\${rabbitmq.exchange.name}")
  private val exchangeName: String,

  @Value("\${rabbitmq.routing.key}")
  private val routingKey: String,

  @Value("\${rabbitmq.routing.json.key}")
  private val routingJsonKey: String
) {

  @Bean
  fun queue(): Queue {
    return Queue(queueName)
  }

  @Bean
  fun jsonQueue(): Queue {
    return Queue(jsonQueueName)
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

  @Bean
  fun jsonBinding(): Binding {
    return BindingBuilder.bind(jsonQueue())
      .to(topicExchange())
      .with(routingJsonKey)
  }

  @Bean
  fun messageConverter(): MessageConverter {
    return Jackson2JsonMessageConverter()
  }

  @Bean
  fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
    val rabbitTemplate = RabbitTemplate(connectionFactory)
    rabbitTemplate.messageConverter = messageConverter()
    return rabbitTemplate
  }
}