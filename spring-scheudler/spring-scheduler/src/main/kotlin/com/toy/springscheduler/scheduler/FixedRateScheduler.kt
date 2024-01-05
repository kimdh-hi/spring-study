package com.toy.springscheduler.scheduler

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FixedRateScheduler {

  private val log = LoggerFactory.getLogger(javaClass)

  // 1초마다 실행
  @Scheduled(fixedRate = 1000)
  fun scheduler() {
    log.info("FixedRateScheduler...")
  }
}