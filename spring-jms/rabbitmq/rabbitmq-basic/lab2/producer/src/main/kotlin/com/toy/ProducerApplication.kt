package com.toy

import com.toy.domain.DummyMessage
import com.toy.producer.ReliableProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProducerApplication(
  private val producer: ReliableProducer
): CommandLineRunner {
  override fun run(vararg args: String) {

    val message1 = DummyMessage("invalid", 1)
    val message2 = DummyMessage("invalid", 2)

    producer.sendMessageWithInvalidRoutingKey(message1)
    producer.sendMessageToNotExistsExchange(message2)
  }
}

fun main(args: Array<String>) {
  runApplication<ProducerApplication>(*args)
}
