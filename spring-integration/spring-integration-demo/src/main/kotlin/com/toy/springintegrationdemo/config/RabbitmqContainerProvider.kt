package com.toy.springintegrationdemo.config

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.stereotype.Component

@Component
class RabbitmqContainerProvider(
  private val rabbitListenerContainerFactory: SimpleRabbitListenerContainerFactory
) {

  fun getListenerContainer(queueName: String): SimpleMessageListenerContainer {
    val container = rabbitListenerContainerFactory.createListenerContainer()
    container.setQueueNames(queueName)
    return container
  }

}