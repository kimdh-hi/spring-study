package com.toy.emailauthentication.common

import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import software.amazon.awssdk.services.ses.SesAsyncClient
import software.amazon.awssdk.services.ses.model.*

@Component
class SesMailSender(
  private val sesAsyncClient: SesAsyncClient,
  private val templateEngine: SpringTemplateEngine,
  private val emailProperties: EmailProperties
) {

  fun send(to: String, subject: String, template:String, context: Context) {
    val html = templateEngine.process(template, context)

    val emailRequest = SendEmailRequest.builder()
      .destination(getDestination(to))
      .source(emailProperties.fromAddress)
      .message(createMessage(subject, html))
      .build()

    sesAsyncClient.sendEmail(emailRequest)
  }

  private fun createMessage(subject: String, html: String): Message {
    val subjectContent = getSubjectContent(subject)

    return Message.builder()
      .subject(subjectContent)
      .body(Body.builder().html { h -> h.data(html) }.build())
      .build()
  }

  private fun getSubjectContent(subject: String): Content {
    return Content.builder()
      .data(subject).build()
  }

  private fun getDestination(toAddress: String)
    = Destination.builder()
    .toAddresses(listOf(toAddress))
    .build()
}