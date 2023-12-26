package com.study.springmdc.mdc

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class MDCLoggingFilter: Filter {

  override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    val uuid = UUID.randomUUID().toString()
    MDC.put("request_id", uuid)
    chain.doFilter(request, response)
    MDC.clear()
  }
}