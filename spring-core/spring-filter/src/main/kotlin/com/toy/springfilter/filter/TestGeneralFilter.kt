package com.toy.springfilter.filter

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

@Component
class TestGeneralFilter: Filter {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    log.info("Called TestGeneralFilter...")
    chain.doFilter(request, response)
  }
}