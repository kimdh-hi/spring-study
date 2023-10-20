package com.lecture.springmvctypeconverter.config

import com.lecture.springmvctypeconverter.converter.IntToStringConverter
import com.lecture.springmvctypeconverter.converter.IpPortToStringConverter
import com.lecture.springmvctypeconverter.converter.StringToIntConverter
import com.lecture.springmvctypeconverter.converter.StringToIpPortConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig: WebMvcConfigurer {

  override fun addFormatters(registry: FormatterRegistry) {
    registry.addConverter(StringToIntConverter())
    registry.addConverter(IntToStringConverter())
    registry.addConverter(IpPortToStringConverter())
    registry.addConverter(StringToIpPortConverter())
  }
}