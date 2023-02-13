package com.toy.springkafka.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate

@Configuration
class ReplyingKafkaTemplateConfig {

  @Bean
  fun replyingKafkaTemplate(
    producerFactory: ProducerFactory<String, String>,
    repliesContainer: ConcurrentMessageListenerContainer<String, String>
  ): ReplyingKafkaTemplate<String, String, String> {
    return ReplyingKafkaTemplate(producerFactory(), repliesContainer)
  }

  @Bean
  fun repliesContainer(
    containerFactory: ConcurrentKafkaListenerContainerFactory<String, String>
  ): ConcurrentMessageListenerContainer<String, String> {
    val container = containerFactory.createContainer("newTopics-replies")
    container.containerProperties.setGroupId("replies-container-id")
    return container
  }

  private fun producerFactory(): ProducerFactory<String, String> {
    return DefaultKafkaProducerFactory(producerProperties())
  }

  @Bean
  fun producerProperties(): HashMap<String, Any> {
    return hashMapOf(
      ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
    )
  }
}