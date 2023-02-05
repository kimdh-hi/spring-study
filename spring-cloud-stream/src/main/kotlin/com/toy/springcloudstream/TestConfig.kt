package com.toy.springcloudstream

import org.slf4j.LoggerFactory
import org.springframework.cglib.core.internal.Function
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class TestConfig {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun uppercase(): Function<String, String> = Function { message: String ->
    log.info("Converting {} to uppercase", message)
    message.uppercase(Locale.getDefault())
  }

  @Bean
  fun reverse(): Function<String, String> = Function { message: String ->
    log.info("Reversing {}", message)
    StringBuilder(message).reverse().toString()
  }
}
