package com.toy.springtestcontainer.config

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.autoconfigure.amqp.RabbitProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmqpConfig(
  private val rabbitProperties: RabbitProperties
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun cachingConnectionFactory(): ConnectionFactory {
    return CachingConnectionFactory().apply {
      log.info("[Rabbitmq] create ConnectionFactory [address: ${rabbitProperties.addresses}]")
      setAddresses(rabbitProperties.addresses)
      username = rabbitProperties.username
      setPassword(rabbitProperties.password)

      afterPropertiesSet()
    }
  }

  @Bean
  fun rabbitTemplate(): RabbitTemplate {
    return RabbitTemplate(cachingConnectionFactory())
  }

}