package com.toy.kotlinlogging

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FluentLoggingTest {

  private val log = logger()

  @Test
  fun test() {
    log.atDebug().log { printLog("data1") } // fluent logging
    log.debug("{}", printLog("data2"))
  }

  private fun printLog(data: String): String {
    return """
      log..... data=$data
    """.trimIndent()
  }
}
