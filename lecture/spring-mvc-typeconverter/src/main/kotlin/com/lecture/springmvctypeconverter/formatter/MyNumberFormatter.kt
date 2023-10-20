package com.lecture.springmvctypeconverter.formatter

import org.slf4j.LoggerFactory
import org.springframework.format.Formatter
import java.text.NumberFormat
import java.util.*

//"1,000" -> 1000
class MyNumberFormatter: Formatter<Number> {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun print(`object`: Number, locale: Locale): String {
    log.info("object=$`object`, locale=$locale")
    val format = NumberFormat.getInstance()
    return format.format(`object`)
  }

  override fun parse(text: String, locale: Locale): Number {
    log.info("test=$text, locale=$locale")
    val format = NumberFormat.getInstance()
    return format.parse(text)
  }
}