package com.toy.springmqtt.config

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class IntegrationConfig {

  @PostConstruct
  fun createMqttFlow() {

  }
}