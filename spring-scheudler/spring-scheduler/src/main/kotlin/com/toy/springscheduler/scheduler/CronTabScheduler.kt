package com.toy.springscheduler.scheduler

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CronTabScheduler {

  private val log = LoggerFactory.getLogger(javaClass)

  @Scheduled(cron = "0 0 11 * * *")
  fun job() {
    log.info("job...")
  }

  @Scheduled(cron = "\${schedulers.cron.test-job}")
  fun job2() {
    log.info("job2...")
  }
}