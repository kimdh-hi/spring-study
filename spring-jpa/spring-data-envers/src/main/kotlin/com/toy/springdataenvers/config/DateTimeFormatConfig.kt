package com.toy.springdataenvers.config

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class DateTimeFormatConfig : WebMvcConfigurer {
  override fun addFormatters(registry: FormatterRegistry) {
    val registrar = DateTimeFormatterRegistrar()
    registrar.setUseIsoFormat(true)
    registrar.registerFormatters(registry)
  }
}