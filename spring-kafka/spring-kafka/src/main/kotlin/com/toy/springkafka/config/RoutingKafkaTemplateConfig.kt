package com.toy.springkafka.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.core.RoutingKafkaTemplate
import java.util.regex.Pattern

@Configuration
class RoutingKafkaTemplateConfig {

  @Bean
  fun routingKafkaTemplate(): RoutingKafkaTemplate {
    return RoutingKafkaTemplate(factories())
  }

  // 어떤 타입으로 직렬화될지 알 수 없기 때문에 any 타입
  private fun factories(): LinkedHashMap<Pattern, ProducerFactory<Any, Any>> {
    return linkedMapOf(
      Pattern.compile("newTopics1-bytes") to byteProducerFactory(),
      Pattern.compile(".*") to defaultProducerFactory(),
    )
  }

  private fun defaultProducerFactory(): ProducerFactory<Any, Any> {
    return DefaultKafkaProducerFactory(producerProperties())
  }

  fun byteProducerFactory(): ProducerFactory<Any, Any> {
    val properties = producerProperties()
    properties[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = ByteArraySerializer::class.java
    return DefaultKafkaProducerFactory(properties)
  }

  private fun producerProperties(): HashMap<String, Any> {
    return hashMapOf(
      ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
    )
  }
}