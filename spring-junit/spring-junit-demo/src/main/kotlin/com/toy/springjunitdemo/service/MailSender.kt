package com.toy.springjunitdemo.service

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class MailSender {

  @Async
  fun sendNotification() {
    throw IllegalArgumentException("...")
  }
}