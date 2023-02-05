package com.toy.springkafka.consumer

import com.toy.springkafka.model.User
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Component
import java.util.*

@Component
class TestConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

  @KafkaListener(id = "test-id1", topics = ["test-topic"])
  fun listener1(message: String) {
    log.info("message: {}", message)
  }

  @KafkaListener(id = "test-id2", topics = ["newTopics1"])
  fun listener2(message: String) {
    log.info("consumer newTopics1 {}", message)
  }

  @KafkaListener(id = "test-id3", topics = ["newTopics1-bytes"])
  fun listener3(message: String) {
    log.info("consumer newTopics1-bytes {}", message)
  }

  @KafkaListener(id = "test-id4", topics = ["newTopics1-replies"])
  @SendTo
  fun listener4(message: String): String {
    log.info("consumer newTopics1-replies {}", message)
    return "pong..."
  }

  @KafkaListener(id = "consumerTestId", topics = ["consumerTest"])
  fun consumerTestListener(
    message: String,
    @Header(KafkaHeaders.RECEIVED_TIMESTAMP) timestamp: Long,
    metadata: ConsumerRecordMetadata
  ) {
    log.info("consumerTestListener message: {}", message)
    log.info("consumerTestListener timestamp: {}", Date(timestamp))
    log.info("consumerTestListener metadata: {}", metadata.offset())
  }

  @KafkaListener(id = "userConsumerId", topics = ["userTopic"], containerFactory = "kafkaJsonContainerFactory")
  fun listenUser(user: User) {
    log.info("listenUser: {}", user)
  }
}