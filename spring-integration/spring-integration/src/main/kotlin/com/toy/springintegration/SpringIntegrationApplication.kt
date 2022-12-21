package com.toy.springintegration

import com.toy.springintegration.config.MqttConfig
import com.toy.springintegration.config.SampleMessage
import com.toy.springintegration.constants.MqttTopic
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringIntegrationDemoApplication

fun main(args: Array<String>) {
  runApplication<SpringIntegrationDemoApplication>(*args)
}

@Component
class ApplicationRunner(
  private val mqttOutboundGateway: MqttConfig.MqttOutboundGateway
): CommandLineRunner
{
  override fun run(vararg args: String) {
    mqttOutboundGateway.publish(MqttTopic.TEST.format, SampleMessage("test"))
  }
}