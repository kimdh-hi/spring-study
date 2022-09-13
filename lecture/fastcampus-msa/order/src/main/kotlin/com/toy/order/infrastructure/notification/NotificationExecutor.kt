package com.toy.order.infrastructure.notification

import com.toy.order.domain.notification.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class NotificationExecutor: NotificationService {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun sendEmail(email: String, title: String, description: String) {
    log.info("send Email")
  }

  override fun sendKakao(phoneNo: String, description: String) {
    log.info("send Kakao")
  }

  override fun sendSms(phoneNo: String, description: String) {
    log.info("send Sms")
  }
}