package com.toy.springmvc.config

import com.toy.springmvc.converter.StringToEnumConverterFactory
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MvcConfig: WebMvcConfigurer {

  override fun addFormatters(registry: FormatterRegistry) {
    registry.addConverterFactory(StringToEnumConverterFactory)
  }
}