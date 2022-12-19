package com.toy.springintegrationdemo.config

import com.toy.springintegrationdemo.config.activator.MqttTestHandler
import com.toy.springintegrationdemo.constants.MqttChannelConstants
import com.toy.springintegrationdemo.constants.MqttTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.Transformers
import org.springframework.integration.dsl.integrationFlow
import org.springframework.integration.mqtt.core.MqttPahoClientFactory
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter
import java.util.*

@Configuration
class MqttIntegrationConfig(
  private val mqttPahoClientFactory: MqttPahoClientFactory,
  private val mqttTestHandler: MqttTestHandler
) {

  @Bean
  fun inboundMultiLevelFlow() = integrationFlow(
    createMqttInboundAdapter(mqttPahoClientFactory, MqttTopic.MULTI_LEVEL_TEST.format)
  ) {
    transform(Transformers.fromJson(SampleMessage::class.java))
    handle { mqttTestHandler.multiLevelTestExecute(it.payload as SampleMessage) }
  }

  @Bean
  fun inboundSingleLevelFlow() = integrationFlow(
    createMqttInboundAdapter(mqttPahoClientFactory, MqttTopic.SINGLE_LEVEL_TEST.format)
  ) {
    transform(Transformers.fromJson(SampleMessage::class.java))
    handle { mqttTestHandler.singleLevelTestExecute(it.payload as SampleMessage) }
  }

  private fun createMqttInboundAdapter(
    mqttPahoClientFactory: MqttPahoClientFactory,
    topic: String, qos: Int = 0, completionTimeoutMills: Long? = 5000L
  ): MqttPahoMessageDrivenChannelAdapter {
    val adapter = MqttPahoMessageDrivenChannelAdapter(
      generateClientId(),
      mqttPahoClientFactory,
      topic
    )

    completionTimeoutMills?.let { adapter.setCompletionTimeout(it) }
    adapter.setConverter(DefaultPahoMessageConverter())
    adapter.setQos(qos)
    adapter.setErrorChannelName(MqttChannelConstants.ERROR_CHANNEL)

    return adapter
  }

  private fun generateClientId(prefix: String = ""): String {
    return "$prefix${UUID.randomUUID()}"
  }
}