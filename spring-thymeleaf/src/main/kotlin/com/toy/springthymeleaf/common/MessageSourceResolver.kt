package com.toy.springthymeleaf.common

import org.springframework.context.MessageSource
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.stereotype.Component

@Component
class MessageSourceResolver(private val messageSource: MessageSource) {

  val messages: MessageSourceAccessor by lazy {
    MessageSourceAccessor(messageSource)
  }

  fun getMessage(code: String) = messages.getMessage(code)

  fun getMessage(code: String, vararg params: String) = messages.getMessage(code, params)

  fun getMessage(code: String, params: List<String>) = messages.getMessage(code, params.toTypedArray())
}