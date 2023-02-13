package com.toy.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Declarables
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitmqSchemaConfig {

  @Bean
  fun rabbitmqSchema(): Declarables {
    return Declarables(
      FanoutExchange("x.another-dummy", true, false),
      Queue("q.another-dummy"),
      Binding("q.another-dummy", Binding.DestinationType.QUEUE, "x.another-dummy", "", null)
    )
  }

//  @Bean
//  fun fanoutExchange(): FanoutExchange {
//    return FanoutExchange("x.another-dummy", true, false)
//  }
//
//  @Bean
//  fun directExchange(): DirectExchange {
//    return DirectExchange("x.another-direct-exchange")
//  }
//
//  @Bean
//  fun queueAnotherDummy(): Queue {
//    return Queue("x.another-queue")
//  }
//
//  @Bean
//  fun binding(): Binding {
//    return BindingBuilder
//      .bind(queueAnotherDummy())
//      .to(fanoutExchange())
//  }
//
//  @Bean
//  fun bindingToDirect(): Binding {
//    return BindingBuilder
//      .bind(queueAnotherDummy())
//      .to(directExchange())
//      .with("key1")
//  }
}