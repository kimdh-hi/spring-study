package com.toy.springthymeleaf.mail

import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import java.util.*

@Component
class MailSender(
  private val templateEngine: SpringTemplateEngine,
) {

  fun send(to: String, templateName: String, context: Context, language: String) {
    context.locale = Locale(language)
    templateEngine.process(templateName, context)
    //mail send
  }

  fun send(to: String, templateName: String, context: Map<String, String>, language: String) {
    templateEngine.process(templateName, convertContext(context, language))
    //mail send
  }

  private fun convertContext(context: Map<String, String>, language: String) = Context().apply {
    context.forEach { (key, value) -> setVariable(key, value) }
    locale = Locale(language)
  }
}