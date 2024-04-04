package com.lecture.springcloudstream.sec01

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer
import java.util.function.Function

@Configuration
class KafkaConsumer {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun consumer(): Consumer<String> = Consumer {
    log.info("consumer received: $it")
  }

  @Bean
  fun function() = Function<String, Unit> {
    log.info("function received: $it")
  }
}