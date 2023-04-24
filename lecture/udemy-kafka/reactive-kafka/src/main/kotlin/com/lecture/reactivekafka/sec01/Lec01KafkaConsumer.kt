package com.lecture.reactivekafka.sec01

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions

private val log = LoggerFactory.getLogger(Lec01KafkaConsumer::class.java)

class Lec01KafkaConsumer

fun main() {

  val consumerConfig = mapOf(
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class,
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class,
    ConsumerConfig.GROUP_ID_CONFIG to "demo-group"
  )

  val receiverOptions = ReceiverOptions.create<String, Any>(consumerConfig)
    .subscription(listOf("order-event"))

  KafkaReceiver.create(receiverOptions)
    .receive()
    .doOnNext { log.info("key={}, value={}", it.key(), it.value()) }
    .subscribe()
}