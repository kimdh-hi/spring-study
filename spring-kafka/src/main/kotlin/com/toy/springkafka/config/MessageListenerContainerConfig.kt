package com.toy.springkafka.config

import com.toy.springkafka.consumer.listener.DefaultMessageListener
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import java.io.Serializable

@Configuration
class MessageListenerContainerConfig {

  @Bean
  fun kafkaMessageListenerContainer(): KafkaMessageListenerContainer<String, String> {
    val properties = ContainerProperties("messageListenerTestTopic")
    properties.setGroupId("testTopic-container") // 필수
    properties.ackMode = ContainerProperties.AckMode.BATCH
    properties.messageListener = DefaultMessageListener()

    return KafkaMessageListenerContainer(containerFactory(), properties)
  }

  private fun containerFactory(): ConsumerFactory<String, String> {
    return DefaultKafkaConsumerFactory(consumerFactoryProperties())
  }

  private fun consumerFactoryProperties(): Map<String, Serializable> {
    return hashMapOf(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
    )
  }
}