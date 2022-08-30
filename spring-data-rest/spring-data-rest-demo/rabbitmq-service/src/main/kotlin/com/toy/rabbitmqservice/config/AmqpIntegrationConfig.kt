package com.toy.rabbitmqservice.config

import com.toy.rabbitmqservice.domain.TestMessage
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.amqp.dsl.Amqp
import org.springframework.integration.annotation.IntegrationComponentScan
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.stereotype.Component
import java.util.concurrent.ThreadLocalRandom

@Configuration
@EnableRabbit
@EnableIntegration
@IntegrationComponentScan
class AmqpIntegrationConfig(
  private val connectionFactory: CachingConnectionFactory,
  private val amqpIntgTestHandler: AmpqIntgTestHandler,
  private val rabbitTemplate: RabbitTemplate
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun amqpIntgTestQueue(): Queue {
    return Queue("amqp-intg-test")
  }

  @Bean
  fun amqpIntgTestExchange(): TopicExchange {
    return TopicExchange("amqp-intg-test-exchange")
  }

  @Bean
  fun amqpIntgTestBinding(): Binding {
    return BindingBuilder.bind(amqpIntgTestQueue())
      .to(amqpIntgTestExchange())
      .with("")
  }

  @Bean
  fun testInboundFlow(): IntegrationFlow =
    IntegrationFlows.from(
      Amqp.inboundAdapter(connectionFactory, amqpIntgTestQueue())
        .configureContainer {
          it.concurrentConsumers(2)
          it.defaultRequeueRejected(false)
          it.prefetchCount(100)
        }
    )
//      .handle { message -> log.info("testInboundFlow.handle payload: {}, headers: {}", message.payload, message.headers) }
      .handle(amqpIntgTestHandler, AmpqIntgTestHandler.handleMethodName)
      .get()
}

@Component
class AmpqIntgTestHandler(
  private val rabbitTemplate: RabbitTemplate
) {
  private val amqpSender = AmqpSender(rabbitTemplate, "amqp-intg")

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    const val handleMethodName = "handleMaster"

  }

  fun handleMaster(message: TestMessage) {
    log.info("[AmpqIntgTestHandler] call message: {}", message)
    val queueName = "amqp-intg-test${ThreadLocalRandom.current().nextInt(1, 100) % 3}"
    amqpSender.send(queueName, message)
  }

  @ServiceActivator
  fun handle(message: TestMessage) {
    log.info("[AmpqIntgTestHandler] ServiceActivator call message: {}", message)
  }
}
