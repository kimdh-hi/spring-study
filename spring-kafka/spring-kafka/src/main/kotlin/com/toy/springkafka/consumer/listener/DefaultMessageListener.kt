package com.toy.springkafka.consumer.listener

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.listener.MessageListener

class DefaultMessageListener: MessageListener<String, String> {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun onMessage(data: ConsumerRecord<String, String>) {
    log.info("DefaultMessageListener.onMessage: {}", data.value())
  }
}