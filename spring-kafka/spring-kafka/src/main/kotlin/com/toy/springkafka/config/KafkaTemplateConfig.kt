package com.toy.springkafka.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaTemplateConfig {

  @Bean
  fun kafkaTemplate(): KafkaTemplate<String, String> {
    return KafkaTemplate(producerFactory())
  }

  private fun producerFactory(): ProducerFactory<String, String> {
    return DefaultKafkaProducerFactory(producerProperties())
  }

  private fun producerProperties(): HashMap<String, Any> {
    return hashMapOf(
      ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
    )
  }
}