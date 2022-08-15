package com.toy.producer

import com.toy.producer.domain.DummyMessage
import com.toy.producer.producer.DummyProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDateTime
import java.time.LocalTime

@SpringBootApplication
class ProducerApplication(
  private val producer: DummyProducer
): CommandLineRunner {
  override fun run(vararg args: String?) {
    val message = DummyMessage("message: ${LocalTime.now()}", 1)
    producer.sendMessage(message)
  }
}

fun main(args: Array<String>) {
  runApplication<ProducerApplication>(*args)
}
