package com.toy.order.domain.notification

import org.springframework.stereotype.Service

interface NotificationService {
  fun sendEmail(email: String, title: String, description: String)
  fun sendKakao(phoneNo: String, description: String)
  fun sendSms(phoneNo: String, description: String)
}