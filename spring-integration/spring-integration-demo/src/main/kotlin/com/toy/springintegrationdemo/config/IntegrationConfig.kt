package com.toy.springintegrationdemo.config

import com.toy.springintegrationdemo.config.activator.RequeueTestServiceActivator
import com.toy.springintegrationdemo.config.activator.TestServiceActivator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.amqp.dsl.Amqp
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.handler.LoggingHandler


@Configuration
class IntegrationConfig(
  private val containerProvider: RabbitmqContainerProvider,
  private val testServiceActivator: TestServiceActivator,
  private val requeueTestServiceActivator: RequeueTestServiceActivator
) {

  @Bean
  fun testServiceFlow(): IntegrationFlow = IntegrationFlows.from(
    Amqp.inboundAdapter(
      containerProvider.getListenerContainer("testQueue")
    )
  )
    .wireTap(loggingFlow())
    .handle(testServiceActivator, "testService")
    .get()

  @Bean
  fun loggingFlow(): IntegrationFlow = IntegrationFlows.from("loggingQueue")
      .handle(loggingHandler())
      .get()

  @Bean
  fun loggingHandler(): LoggingHandler {
    val loggingHandler = LoggingHandler(LoggingHandler.Level.INFO.name)
    loggingHandler.setShouldLogFullMessage(true)
    loggingHandler.setLoggerName("Integration-Logger")
    return loggingHandler
  }

  @Bean
  fun requeueTestFlow(): IntegrationFlow = IntegrationFlows.from(
    Amqp.inboundAdapter(containerProvider.getListenerContainer("requeueTestQueue"))
  )
    .handle(requeueTestServiceActivator, "execute")
    .get()

}