package com.toy.springintegrationdemo.config

import com.toy.springintegrationdemo.constants.MqttChannelConstants
import com.toy.springintegrationdemo.constants.MqttTopic
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway
import org.springframework.integration.dsl.integrationFlow
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.core.MqttPahoClientFactory
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.messaging.MessageHandler
import org.springframework.messaging.handler.annotation.Header

@Configuration
class MqttConfig(
  private val mqttProperties: MqttProperties
) {

  companion object {
    const val MQTT_OUTBOUND_CHANNEL = "outboundChannel"
  }

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
  fun mqttOutboundFlow() = integrationFlow(MQTT_OUTBOUND_CHANNEL) {
    handle(mqttOutboundMessageHandler())
  }

  @MessagingGateway(defaultRequestChannel = MQTT_OUTBOUND_CHANNEL)
  interface MqttOutboundGateway {

    @Gateway
    fun publish(@Header(MqttHeaders.TOPIC) topic: String, data: String)
  }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "mqtt")
data class MqttProperties(
  val address: String
)