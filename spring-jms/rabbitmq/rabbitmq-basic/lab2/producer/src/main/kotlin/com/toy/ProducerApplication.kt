package com.toy

import com.toy.domain.DummyMessage
import com.toy.producer.DummyProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
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
