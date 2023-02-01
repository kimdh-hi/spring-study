package com.toy.springkafka

import com.toy.springkafka.producer.TestProducer
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SpringKafkaApplication {

  @Bean
  fun runner(testProducer: TestProducer) = ApplicationRunner {

    testProducer.sendByRoutingTemplate("newTopics1", "test")
    testProducer.sendByRoutingTemplate("newTopics1-bytes", "test".toByteArray())
  }
}

fun main(args: Array<String>) {
  runApplication<SpringKafkaApplication>(*args)
}
