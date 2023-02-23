package com.toy.springapiversioning.config

import com.toy.springapiversioning.aop.ApiVersionProperties
import com.toy.springapiversioning.aop.VersionMappingHandlerMapping
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping


@Configuration
class MvcConfig(
  private val apiVersionProperties: ApiVersionProperties,
): WebMvcRegistrations {

  override fun getRequestMappingHandlerMapping(): RequestMappingHandlerMapping? {
    return VersionMappingHandlerMapping(apiVersionProperties)
  }

}