package com.toy.springintegrationdemo.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.springintegrationdemo.config.activator.MqttTestHandler
import com.toy.springintegrationdemo.constants.MqttChannelConstants
import com.toy.springintegrationdemo.constants.MqttTopic
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway
import org.springframework.integration.dsl.Transformers
import org.springframework.integration.dsl.integrationFlow
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.core.MqttPahoClientFactory
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.messaging.MessageHandler
import org.springframework.messaging.handler.annotation.Header

@Configuration
class MqttConfig(
  private val mqttProperties: MqttProperties,
  private val objectMapper: ObjectMapper,
  private val mqttTestHandler: MqttTestHandler,
) {

  @Bean
  fun mqttPahoClientFactory(): MqttPahoClientFactory = DefaultMqttPahoClientFactory()
    .apply { connectionOptions = connectionOptions() }

  private fun connectionOptions(): MqttConnectOptions = MqttConnectOptions().apply {
    serverURIs = arrayOf(mqttProperties.address)
  }

  @Bean
  fun mqttOutboundMessageHandler(): MessageHandler {
    return MqttPahoMessageHandler(MqttAsyncClient.generateClientId(), mqttPahoClientFactory())
      .apply {
        setAsync(true)
        setDefaultTopic(MqttTopic.DEFAULT.format)
      }
  }

  @Bean
  fun mqttInboundFlow() = integrationFlow(mqttChannelAdapter()) {
    transform(Transformers.fromJson(SampleMessage::class.java))
    handle { mqttTestHandler.execute(it.payload as SampleMessage) }
  }

  private fun mqttChannelAdapter(): MqttPahoMessageDrivenChannelAdapter {
    return MqttPahoMessageDrivenChannelAdapter(
      MqttClient.generateClientId(),
      mqttPahoClientFactory(),
      MqttTopic.TEST.format
    )
      .apply {
        setCompletionTimeout(5000)
      }
  }

  @Bean
  fun mqttOutboundFlow() = integrationFlow(MqttChannelConstants.OUTBOUND_CHANNEL) {
    transform<Any> {
      when (it) {
        is SampleMessage -> objectMapper.writeValueAsString(it)
        else -> it
      }
    }
    handle(mqttOutboundMessageHandler())
  }

  @MessagingGateway(defaultRequestChannel = MqttChannelConstants.OUTBOUND_CHANNEL)
  interface MqttOutboundGateway {

    @Gateway
    fun publish(@Header(MqttHeaders.TOPIC) topic: String, data: String)

    @Gateway
    fun publish(@Header(MqttHeaders.TOPIC) topic: String, sampleMessage: SampleMessage)
  }

}

@ConstructorBinding
@ConfigurationProperties(prefix = "mqtt")
data class MqttProperties(
  val address: String
)

data class SampleMessage(
  val data: String
)