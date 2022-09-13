package com.study.springcoreadvanced.`strategy-pattern`.strategy

import org.slf4j.LoggerFactory
import org.springframework.util.StopWatch

class ContextV1(
  private val strategy: Strategy
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun execute() {
    val stopWatch = StopWatch()
    stopWatch.start()

    // 비즈니스 로직...
    strategy.call()

    stopWatch.stop()
    log.info("execution time: {}", stopWatch.totalTimeMillis)
  }
}