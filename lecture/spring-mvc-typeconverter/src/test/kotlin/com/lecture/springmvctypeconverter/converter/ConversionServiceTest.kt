package com.lecture.springmvctypeconverter.converter

import com.lecture.springmvctypeconverter.type.IpPort
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.convert.support.DefaultConversionService

class ConversionServiceTest {

  @Test
  fun test() {
    val conversionService = DefaultConversionService()
    conversionService.addConverter(StringToIntConverter())
    conversionService.addConverter(IntToStringConverter())
    conversionService.addConverter(IpPortToStringConverter())
    conversionService.addConverter(StringToIpPortConverter())

    val result1 = conversionService.convert("10", Int::class.java)
    val result2 = conversionService.convert(10, String::class.java)
    val result3 = conversionService.convert(IpPort("127.0.0.1", 80), String::class.java)
    val result4 = conversionService.convert("127.0.0.1:80", IpPort::class.java)

    assertThat(result1).isEqualTo(10)
    assertThat(result2).isEqualTo("10")
    assertThat(result3).isEqualTo("127.0.0.1:80")
    assertThat(result4).isEqualTo(IpPort("127.0.0.1", 80))
  }
}