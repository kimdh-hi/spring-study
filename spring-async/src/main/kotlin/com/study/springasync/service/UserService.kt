package com.study.springasync.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService(
  private val mailService: MailService,
) {

  private val log = LoggerFactory.getLogger(UserService::class.java)

  fun signup() {
    log.info("signup")

    mailService.send("welcome.")
  }
}
