package com.toy.kotlinlogging

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LazyLoggerTest {

  private val log by lazyLogger()

  @Test
  fun test() {
    val data = "data"
    log.info("test log data={}", data)
  }
}
