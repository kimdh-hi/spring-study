package com.toy.springkafka.config

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.toy.springkafka.model.User
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListenerConfigurer
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class KafkaJsonListenerContainerConfig(
  private val validator: LocalValidatorFactoryBean
): KafkaListenerConfigurer {

  @Bean
  fun kafkaJsonContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, User>> {
    val factory = ConcurrentKafkaListenerContainerFactory<String, User>()
    factory.consumerFactory = userConsumerFactory()
    return factory
  }

  private fun userConsumerFactory(): ConsumerFactory<String, User> {
    return DefaultKafkaConsumerFactory(
      consumerProperties(),
      StringDeserializer(),
      JsonDeserializer(User::class.java)
    )
  }

  private fun consumerProperties(): HashMap<String, Any> {
    return hashMapOf(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
    )
  }

  override fun configureKafkaListeners(registrar: KafkaListenerEndpointRegistrar) {
    registrar.setValidator(validator)
  }
}