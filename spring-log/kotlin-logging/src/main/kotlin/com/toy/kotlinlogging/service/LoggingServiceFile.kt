package com.toy.kotlinlogging.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

private val kotlinLogger = KotlinLogging.logger {}

@Component
class LoggingService {

  private val logger = LoggerFactory.getLogger(javaClass)

  private val innerKotlinLogger = KotlinLogging.logger {}

  fun stringTemplateLogging(data: String) {
    logger.debug("stringTemplateLogging data=$data")
  }

  fun kotlinLogging(data: String) {
    kotlinLogger.debug { "kotlinLogging data=$data" } // c.t.k.service.LoggingServiceFile: kotlinLogging data=data
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
