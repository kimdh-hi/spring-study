package com.lecture.springmvctypeconverter.converter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConverterTest {

  @Test
  fun stringToInt() {
    val converter = StringToIntConverter()
    val result = converter.convert("1")
    assertThat(result).isEqualTo(1)
  }

  @Test
  fun IntToString() {
    val converter = IntToStringConverter()
    val result = converter.convert(1)
    assertThat(result).isEqualTo("1")
  }
}