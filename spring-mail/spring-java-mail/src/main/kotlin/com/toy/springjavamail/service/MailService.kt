package com.toy.springjavamail.service

import com.toy.springjavamail.common.SesMailSender
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context

@Service
class MailService(
  private val mailSender: SesMailSender
) {

  fun send(to: String, template: String) {

    val context = Context()
    context.setVariable("test", "test-value")

    mailSender.send(
      to = to,
      subject = "test subject",
      template = template,
      context = context)
  }
}