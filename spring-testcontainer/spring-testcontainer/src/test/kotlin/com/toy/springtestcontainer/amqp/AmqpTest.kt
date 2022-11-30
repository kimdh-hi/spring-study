package com.toy.springtestcontainer.amqp

import com.toy.springtestcontainer.base.AbstractRabbitmqTestContainerBase
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.amqp.rabbit.core.RabbitTemplate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AmqpTest(
  private val rabbitTemplate: RabbitTemplate
): AbstractRabbitmqTestContainerBase()  {

  @BeforeAll
  fun beforeAll() {
    RABBITMQ_CONTAINER.start()
  }

  @AfterAll
  fun afterAll() {
    RABBITMQ_CONTAINER.stop()
  }


  @Test
  fun test() {
    rabbitTemplate.convertAndSend("testQueue", "message")
  }
}