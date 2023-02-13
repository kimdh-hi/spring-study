package com.toy.springtestcontainer.base

import org.springframework.amqp.core.Queue
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.TestConstructor
import org.testcontainers.containers.RabbitMQContainer

@SpringBootTest
@TestConfiguration
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
//@Testcontainers
abstract class AbstractRabbitmqTestContainerBase {

  companion object {

//    @Container
    val RABBITMQ_CONTAINER = RabbitMQContainer("rabbitmq:management")
      .withQueue("testWithQueue")
      .withExposedPorts(5672)

  }

  @Bean
  fun testQueue(): Queue {
    return Queue("testQueue")
  }
}