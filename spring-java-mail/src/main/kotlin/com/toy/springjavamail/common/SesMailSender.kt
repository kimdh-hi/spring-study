package com.toy.springjavamail.common

import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import software.amazon.awssdk.services.ses.SesAsyncClient
import software.amazon.awssdk.services.ses.model.Body
import software.amazon.awssdk.services.ses.model.Content
import software.amazon.awssdk.services.ses.model.Destination
import software.amazon.awssdk.services.ses.model.Message
import software.amazon.awssdk.services.ses.model.SendEmailRequest

@Component
class SesMailSender(
  private val sesAsyncClient: SesAsyncClient,
  private val templateEngine: SpringTemplateEngine,
) {

  fun send(to: String, subject: String, template:String, context: Context) {
    val html = templateEngine.process(template, context)

    val emailRequest = SendEmailRequest.builder()
      .destination(getDestination(to))
      .source("zbeld123@gmail.com")
      .message(createMessage(subject, html))
      .build()

    sesAsyncClient.sendEmail(emailRequest)
  }

  private fun createMessage(subject: String, html: String): Message {
    val content = Content.builder()
      .data(subject).build()

    return Message.builder()
      .subject(content)
      .body(Body.builder().html { h -> h.data(html) }.build())
      .build()
  }

  private fun getDestination(toAddress: String)
    = Destination.builder()
      .toAddresses(listOf(toAddress))
      .build()
}