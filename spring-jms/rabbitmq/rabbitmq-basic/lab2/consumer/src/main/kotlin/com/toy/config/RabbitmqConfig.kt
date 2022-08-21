package com.toy.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitmqConfig {

  @Bean
  fun objectMapper() = JsonMapper
    .builder()
    .findAndAddModules()
    .build()

  @Bean
  fun converter(@Autowired objectMapper: ObjectMapper) = Jackson2JsonMessageConverter(objectMapper)

  @Bean
  fun prefetchOne(
    configurer: SimpleRabbitListenerContainerFactoryConfigurer,
    connectionFactory: ConnectionFactory
  ): RabbitListenerContainerFactory<SimpleMessageListenerContainer> {
    val factory = SimpleRabbitListenerContainerFactory()
    configurer.configure(factory, connectionFactory)
    factory.setPrefetchCount(1)
    return factory
  }
}