package com.toy.springmvc.converter

import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory

object StringToEnumConverterFactory : ConverterFactory<String, Enum<*>?> {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun <E : Enum<*>?> getConverter(clazz: Class<E>): Converter<String, E> {
    return StringToEnumConverter(clazz)
  }

  class StringToEnumConverter<T : Enum<*>?>(private val enumType: Class<T>) : Converter<String, T> {
    override fun convert(source: String): T? {

      log.info("StringToEnumConverter source=$source")

      if (source.isEmpty())
        return null

      //대소문자 구분없이 enum convert
      return enumType.enumConstants.first { StringUtils.equalsIgnoreCase(it!!.name, source) }
    }
  }
}