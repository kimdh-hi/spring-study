package com.toy.springmvc.config

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig: WebMvcConfigurer {

  // formatter 빈으로 등록시 추가 설정 필요없음
//  override fun addFormatters(registry: FormatterRegistry) {
//    registry.addFormatter(PersonFormatter())
//  }
}