package com.toy.springapiversioning.aop

import org.apache.commons.lang3.StringUtils
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method

open class VersionMappingHandlerMapping(
  private val apiVersionProperties: ApiVersionProperties,
): RequestMappingHandlerMapping() {
  override fun getMappingForMethod(method: Method, handlerType: Class<*>): RequestMappingInfo? {
    var requestMappingInfo = createRequestMappingInfo(method, handlerType)

    if (requestMappingInfo != null) {
      val apiVersion = AnnotationUtils.getAnnotation(method, ApiVersion::class.java)
        ?: AnnotationUtils.getAnnotation(handlerType, ApiVersion::class.java)

      if (apiVersion != null) {
        val version: String = apiVersion.version.trim()
        var prefix = "/${apiVersionProperties.versionSymbol}$version"
        if (StringUtils.isNoneEmpty(apiVersionProperties.prefix)) {
          prefix = apiVersionProperties.prefix.trim() + prefix
        }

        requestMappingInfo = RequestMappingInfo
          .paths(prefix)
          .options(builderConfiguration)
          .build()
          .combine(requestMappingInfo)
      }
    }

    return requestMappingInfo
  }

  private fun createRequestMappingInfo(method: Method, handlerType: Class<*>): RequestMappingInfo? {
    var requestMappingInfo = createRequestMappingInfo(method)
    if (requestMappingInfo != null) {
      val typeInfo = createRequestMappingInfo(handlerType)
      if (typeInfo != null) {
        requestMappingInfo = typeInfo.combine(requestMappingInfo)
      }
    }

    return requestMappingInfo
  }

  private fun createRequestMappingInfo(element: AnnotatedElement): RequestMappingInfo? {
    val requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping::class.java)
    if (element !is Class<*>)
      (element as Method)
    return if (requestMapping != null ) {
      createRequestMappingInfo(requestMapping, null)
    } else {
      null
    }
  }
}