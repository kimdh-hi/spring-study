package com.study.springasync.service

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class MailService {

  private val log = LoggerFactory.getLogger(MailService::class.java)

  @Async
  fun send(message: String) {
    log.info(
      "MailService send message={}, currentThreadName={}, userIdHolder={}",
      message,
      Thread.currentThread().name,
      UserIdHolder.get()
    )
  }
}
