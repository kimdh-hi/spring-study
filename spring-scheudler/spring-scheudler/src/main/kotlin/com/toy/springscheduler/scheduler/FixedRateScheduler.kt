package com.toy.springscheudler.scheduler

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled

class FixedRateScheduler {

  private val log = LoggerFactory.getLogger(javaClass)

  @Scheduled(fixedRate = 1000)
  fun scheduler() {
    log.info("scheduler...")
  }
}