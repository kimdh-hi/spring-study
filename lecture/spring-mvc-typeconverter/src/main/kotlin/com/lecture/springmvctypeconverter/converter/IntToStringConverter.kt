package com.lecture.springmvctypeconverter.converter

import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter

class IntToStringConverter: Converter<Int, String> {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun convert(source: Int): String {
    log.info("StringToIntConverter.. $source")
    return source.toString()
  }
}