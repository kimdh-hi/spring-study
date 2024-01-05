package com.toy.springscheduler.scheduler

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FixedDelayScheduler {
  private val log = LoggerFactory.getLogger(javaClass)

  // 앞 선 작업 종료 후 1000 만큼 경과 후 시작
  @Scheduled(fixedDelay = 1000)
  fun scheduler() {
    log.info("FixedDelayScheduler...")
    Thread.sleep(1000)
  }
}