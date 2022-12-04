package com.lecture.springkafkalecturefc

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate

@SpringBootApplication
class SpringKafkaLectureFcApplication {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun applicationRunner(kafkaTemplate: KafkaTemplate<String, String>): ApplicationRunner {
    return ApplicationRunner { args ->
      log.info("kafka publishing...")
      kafkaTemplate.send("quick-start", "test")
    }
  }
}

fun main(args: Array<String>) {
  runApplication<SpringKafkaLectureFcApplication>(*args)
}
