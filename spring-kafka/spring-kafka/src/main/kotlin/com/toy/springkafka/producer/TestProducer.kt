package com.toy.springkafka.producer

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.RoutingKafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFutureCallback

@Component
class TestProducer(
  private val kafkaTemplate: KafkaTemplate<String, String>,
  private val routingKafkaTemplate: RoutingKafkaTemplate
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun async(topic: String, message: String) {
    val future = kafkaTemplate.send(topic, message)
    future.addCallback(callBack(message))
  }

  private fun callBack(message: String) = object : ListenableFutureCallback<SendResult<String, String>> {
    override fun onSuccess(result: SendResult<String, String>?) {
      log.info("onSuccess  result: {}, message: {}", result, message)
    }
    override fun onFailure(e: Throwable) {
      log.error("ERROR ${e.message}")
    }
  }

  fun sendByRoutingTemplate(topic: String, message: String) {
    routingKafkaTemplate.send(topic, message)
  }

  fun sendByRoutingTemplate(topic: String, message: ByteArray) {
    routingKafkaTemplate.send(topic, message)
  }
}