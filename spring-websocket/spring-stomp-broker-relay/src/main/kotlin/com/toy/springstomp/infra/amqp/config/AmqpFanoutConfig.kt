package com.toy.springstomp.infra.amqp.config

import com.toy.springstomp.infra.amqp.AmqpExchanges
import org.springframework.amqp.core.AnonymousQueue
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmqpFanoutConfig {
  @Bean
  fun duplicateSessionExchange(): FanoutExchange = FanoutExchange(AmqpExchanges.DUPLICATE_SESSION)

  @Bean
  fun duplicateSessionQueue(): AnonymousQueue = AnonymousQueue()

  @Bean
  fun duplicateSessionBinding(duplicateSessionQueue: AnonymousQueue, duplicateSessionExchange: FanoutExchange): Binding =
    BindingBuilder.bind(duplicateSessionQueue).to(duplicateSessionExchange)
}
