package com.toy

import com.toy.domain.DummyMessage
import com.toy.producer.DummyMultiplePrefetchProducer
import com.toy.producer.DummyPrefetchProducer
import com.toy.producer.DummyProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalTime

@SpringBootApplication
class ProducerApplication(
  private val producer: DummyMultiplePrefetchProducer
): CommandLineRunner {
  override fun run(vararg args: String?) {
    producer.sendLongTimeProcessingMessage()
    producer.sendShortTimeProcessingMessage()
  }
}

fun main(args: Array<String>) {
  runApplication<ProducerApplication>(*args)
}
