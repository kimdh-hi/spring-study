package com.toy.kotlinlogging

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LazyKotlinLoggerTest {

  private val log by lazyKotlinLogging()

  @Test
  fun test() {
    val data = "data"
    log.info { "test log data=$data" }
  }
}
