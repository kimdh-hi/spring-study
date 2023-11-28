package com.toy.springthymeleaf.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
class MessageSourceConfig {

  // spring-boot 자동설정
  // setBasenames("messages")
  // spring.messages.basename=messages
//  @Bean
//  fun messageSource() = ResourceBundleMessageSource().apply {
//      setBasenames("messages", "errors")
//      setDefaultEncoding("utf-8")
//    }
}