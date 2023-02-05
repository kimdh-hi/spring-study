package com.toy.springkafka

import com.toy.springkafka.model.User
import com.toy.springkafka.producer.TestProducer
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.listener.KafkaMessageListenerContainer

@SpringBootApplication
class SpringKafkaApplication {

  @Bean
  fun runner(
    testProducer: TestProducer,
    kafkaMessageListenerContainer: KafkaMessageListenerContainer<String, String>
  ) = ApplicationRunner {
    testProducer.async("userTopic", User(name = "user", age = 28))
  }
}

fun main(args: Array<String>) {
  runApplication<SpringKafkaApplication>(*args)
}
