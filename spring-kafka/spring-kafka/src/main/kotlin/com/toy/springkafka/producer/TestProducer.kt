package com.toy.springkafka.producer

import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.RoutingKafkaTemplate
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFutureCallback
import java.util.concurrent.TimeUnit

@Component
class TestProducer(
  private val kafkaTemplate: KafkaTemplate<String, String>,
  private val routingKafkaTemplate: RoutingKafkaTemplate,
  private val replyingKafkaTemplate: ReplyingKafkaTemplate<String, String, String>
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

  fun sendByReplyingKafkaTemplate(topic: String, message: String) {
    val record = ProducerRecord<String, String>(topic, message)
    val replyFuture = replyingKafkaTemplate.sendAndReceive(record)
    val replyResponse = replyFuture.get(10, TimeUnit.SECONDS)
    log.info("reply: {}", replyResponse?.value())
  }
}