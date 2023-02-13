package com.toy.springmqtt.config

import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory

@Configuration
class MqttConfig {
  companion object {
    private const val MQTT_USERNAME = "username"
    private const val MQTT_PASSWORD = "password"
  }

  @Bean
  fun defaultMqttPahoClientFactory() = DefaultMqttPahoClientFactory().apply {
    connectionOptions = connectOptions()
  }

  private fun connectOptions() = MqttConnectOptions().apply {
    isCleanSession = true
    userName = MQTT_USERNAME
    password = MQTT_PASSWORD.toCharArray()
  }
}