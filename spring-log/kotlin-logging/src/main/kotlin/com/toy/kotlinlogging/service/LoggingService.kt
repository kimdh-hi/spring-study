package com.toy.kotlinlogging.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

//https://github.com/oshai/kotlin-logging/issues/364
//top-level 선언 권장
private val kotlinLogger = KotlinLogging.logger {}

@Component
class LoggingService {

  private val logger = LoggerFactory.getLogger(javaClass)

  private val innerKotlinLogger = KotlinLogging.logger {}

  fun stringTemplateLogging(data: String) {
    val data = TestData("test")
    logger.debug("stringTemplateLogging data=$data")
    logger.info("stringTemplateLogging data=$data")
    logger.warn("stringTemplateLogging data=$data")
  }

  fun kotlinLogging(data: String) {
    val data = TestData("test")
    kotlinLogger.debug { "kotlinLogging data=$data" } // c.t.k.service.LoggingServiceFile: kotlinLogging data=data
    kotlinLogger.info { "kotlinLogging data=$data" }
    kotlinLogger.warn { "kotlinLogging data=$data" }
    innerKotlinLogger.debug { "kotlinLogging data=$data" } // c.t.k.service.LoggingService: kotlinLogging data=data
  }

  fun kotlinErrorLogging(data: String) {
    kotlinLogger.debug { "kotlinLogging data=$data" }
    runCatching {
      if (data == "error") {
        throw IllegalArgumentException("error...")
      }
    }.onFailure {
      kotlinLogger.error(it) { "error.........." }
    }
  }
}

data class TestData(
  val data: String
) {
  override fun toString(): String {
    return "TestData(data='$data')" // toString 호출 여부 확인
  }
}
