package com.toy.springintegrationdemo

import com.toy.springintegrationdemo.config.MqttConfig
import com.toy.springintegrationdemo.config.SampleMessage
import com.toy.springintegrationdemo.constants.MqttTopic
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