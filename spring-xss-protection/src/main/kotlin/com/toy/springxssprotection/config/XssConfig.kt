package com.toy.springxssprotection.config

import com.fasterxml.jackson.core.SerializableString
import com.fasterxml.jackson.core.io.CharacterEscapes
import com.fasterxml.jackson.core.io.SerializedString
import com.fasterxml.jackson.databind.ObjectMapper
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter
import org.apache.commons.text.StringEscapeUtils
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.Serial

@Configuration
class XssConfig: WebMvcConfigurer {

  @Bean
  fun filterFilterRegistrationBean(): FilterRegistrationBean<XssEscapeServletFilter> {
    val filterRegistration = FilterRegistrationBean<XssEscapeServletFilter>()
    filterRegistration.filter = XssEscapeServletFilter()
    filterRegistration.order = 1
    filterRegistration.addUrlPatterns("/*")
    return filterRegistration
  }

  @Bean
  fun jsonEscapeConverter(objectMapper: ObjectMapper): MappingJackson2HttpMessageConverter {
    objectMapper.factory.characterEscapes = HtmlCharacterEscapes()
    return MappingJackson2HttpMessageConverter(objectMapper)
  }

}

class HtmlCharacterEscapes: CharacterEscapes() {

  private val asciiEscapes: IntArray = standardAsciiEscapesForJSON()

  init {
    asciiEscapes['<'.code] = ESCAPE_CUSTOM
    asciiEscapes['>'.code] = ESCAPE_CUSTOM
    asciiEscapes['\"'.code] = ESCAPE_CUSTOM
    asciiEscapes['('.code] = ESCAPE_CUSTOM
    asciiEscapes[')'.code] = ESCAPE_CUSTOM
    asciiEscapes['#'.code] = ESCAPE_CUSTOM
    asciiEscapes['\''.code] = ESCAPE_CUSTOM
  }

  override fun getEscapeCodesForAscii(): IntArray = asciiEscapes

  override fun getEscapeSequence(ch: Int): SerializableString = SerializedString(StringEscapeUtils.escapeHtml4(Character.toString(ch)))

  companion object {
    @Serial
    private const val serialVersionUID: Long = -4878774421974479870L
  }
}