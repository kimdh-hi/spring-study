package com.toy.springmvc.interceptors

import org.slf4j.LoggerFactory
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AnotherSampleInterceptor: HandlerInterceptor {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
    log.info("[AnotherSampleInterceptor] preHandle")
    return super.preHandle(request, response, handler)
  }

  override fun postHandle(
    request: HttpServletRequest,
    response: HttpServletResponse,
    handler: Any,
    modelAndView: ModelAndView?
  ) {
    log.info("[AnotherSampleInterceptor] postHandle")
    super.postHandle(request, response, handler, modelAndView)
  }

  override fun afterCompletion(
    request: HttpServletRequest,
    response: HttpServletResponse,
    handler: Any,
    ex: Exception?
  ) {
    log.info("[AnotherSampleInterceptor] afterCompletion")
    super.afterCompletion(request, response, handler, ex)
  }
}