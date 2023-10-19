package com.lecture.springmvctypeconverter.converter

import com.lecture.springmvctypeconverter.type.IpPort
import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter

class IpPortToStringConverter: Converter<IpPort, String> {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun convert(source: IpPort): String {
    return source.ip + ":" + source.port.toString()
  }
}