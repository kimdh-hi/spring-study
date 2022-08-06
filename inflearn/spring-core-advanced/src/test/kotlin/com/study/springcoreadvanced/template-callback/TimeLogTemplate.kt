package com.study.springcoreadvanced.`template-callback`

import org.slf4j.LoggerFactory
import org.springframework.util.StopWatch

class TimeLogTemplate {

  private val log = LoggerFactory.getLogger(javaClass)

  fun execute(callback: Callback) {
    val stopWatch = StopWatch()
    stopWatch.start()

    // 비즈니스 로직...
    callback.call()

    stopWatch.stop()
    log.info("execution time: {}", stopWatch.totalTimeMillis)
  }
}