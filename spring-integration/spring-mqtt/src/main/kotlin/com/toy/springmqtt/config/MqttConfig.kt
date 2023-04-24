package com.toy.springmqtt.config

import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.messaging.handler.annotation.Header

@MessagingGateway(defaultRequestChannel = MQTT_OUTBOUND_CHANNEL)
interface MqttOutboundGateway {
  @Gateway
  fun publish(@Header(MqttHeaders.TOPIC) topic: String, data: String)
}

const val MQTT_OUTBOUND_CHANNEL = "mqttOutboundChannel"

fun createMqttClientFactory(brokerUrls: List<String>? = null) = DefaultMqttPahoClientFactory().apply {
  connectionOptions = getConnectionOptions(brokerUrls)
}

fun getConnectionOptions(brokerUrls: List<String>? = null) = MqttConnectOptions().apply {
  brokerUrls?.let { serverURIs = it.toTypedArray() }
  maxInflight = 30
}