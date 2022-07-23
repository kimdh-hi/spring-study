package com.toy.springthymeleaf.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
class MessageSourceConfig {

  companion object {
    const val LANGUAGE_MESSAGE_SOURCE_PATH = "messages.messages"
  }

  @Bean
  fun messageSource(): ResourceBundleMessageSource {
    val messageSource = ResourceBundleMessageSource()
    messageSource.addBasenames(
      LANGUAGE_MESSAGE_SOURCE_PATH
    )
    messageSource.setUseCodeAsDefaultMessage(true)
    return messageSource
  }
}