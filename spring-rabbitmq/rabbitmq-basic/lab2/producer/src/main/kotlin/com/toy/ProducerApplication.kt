package com.toy

import com.toy.domain.DummyMessage
import com.toy.producer.AnotherDummyProducer
import com.toy.producer.ReliableProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProducerApplication(
  private val producer: AnotherDummyProducer
): CommandLineRunner {
  override fun run(vararg args: String) {
    val dummyMessage = DummyMessage("dummy..", 1)
    producer.sendMessage(dummyMessage)
  }
}

fun main(args: Array<String>) {
  runApplication<ProducerApplication>(*args)
}
