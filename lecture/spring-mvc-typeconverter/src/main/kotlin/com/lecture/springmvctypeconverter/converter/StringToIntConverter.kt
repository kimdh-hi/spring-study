package com.lecture.springmvctypeconverter.converter

import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter

class StringToIntConverter: Converter<String, Int> {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun convert(source: String): Int {
    log.info("StringToIntConverter.. $source")
    return source.toInt()
  }
}