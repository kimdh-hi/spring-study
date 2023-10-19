package com.lecture.springmvctypeconverter.converter

import com.lecture.springmvctypeconverter.type.IpPort
import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter

class StringToIpPortConverter: Converter<String, IpPort> {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun convert(source: String): IpPort {
    log.info("StringToIpPortConverter.. $source")
    val splitSource = source.split(":")
    val ip = splitSource[0]
    val port = splitSource[1].toInt()

    return IpPort(ip, port)
  }
}