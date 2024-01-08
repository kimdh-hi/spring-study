package com.toy.springscheduler.scheduler

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ThreadPoolScheduler {

  private val log = LoggerFactory.getLogger(javaClass)

  @Async
  @Scheduled(fixedDelay = 1000)
  fun scheduler1() {
    log.info("scheduler1...")
    Thread.sleep(10_000)
  }

  @Async
  @Scheduled(fixedDelay = 1000)
  fun scheduler2() {
    log.info("scheduler2...")
  }
}