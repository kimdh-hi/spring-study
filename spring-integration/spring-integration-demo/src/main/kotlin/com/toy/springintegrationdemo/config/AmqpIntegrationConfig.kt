package com.toy.springintegrationdemo.config

import com.toy.springintegrationdemo.config.activator.RequeueTestServiceActivator
import com.toy.springintegrationdemo.config.activator.TestServiceActivator
import com.toy.springintegrationdemo.controller.RouteTestMessage
import com.toy.springintegrationdemo.controller.RouteType
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.amqp.dsl.Amqp
import org.springframework.integration.dsl.*
import org.springframework.integration.handler.LoggingHandler
import org.springframework.integration.router.MethodInvokingRouter
import java.util.function.Consumer


@Configuration
class AmqpIntegrationConfig(
  private val containerProvider: RabbitmqContainerProvider,
  private val testServiceActivator: TestServiceActivator,
  private val requeueTestServiceActivator: RequeueTestServiceActivator,
) {

  private val log = LoggerFactory.getLogger(javaClass)

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
    .filter("(headers['x-death'] != null) ? headers['x-death'][0].count <= 3: true") {
      log.info(it.toString())
    }
    .get()

  @Bean
  fun typeBaseRouteTestFlow(): IntegrationFlow = IntegrationFlows.from(
    Amqp.inboundAdapter(containerProvider.getListenerContainer("typeBaseRouteTestQueue"))
  )
    .route(RouteTestMessage::type) { mapping ->
      mapping
        .subFlowMapping(RouteType.TYPE1) { flow -> flow.handle(testServiceActivator, "type1Handler") }
        .subFlowMapping(RouteType.TYPE2) { flow -> flow.handle(testServiceActivator, "type2Handler") }
    }
    .get()

}