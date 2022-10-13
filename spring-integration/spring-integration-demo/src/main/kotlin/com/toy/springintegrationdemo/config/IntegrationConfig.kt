package com.toy.springintegrationdemo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.amqp.dsl.Amqp
import org.springframework.integration.dsl.IntegrationFlows

@Configuration
class IntegrationConfig(
  private val containerProvider: RabbitmqContainerProvider,
  private val testServiceActivator: TestServiceActivator
) {

  @Bean
  fun testServiceFlow() = IntegrationFlows.from(
    Amqp.inboundAdapter(
      containerProvider.getListenerContainer("testQueue")
    )
  )
    .handle(testServiceActivator, "testService")
    .get()
}