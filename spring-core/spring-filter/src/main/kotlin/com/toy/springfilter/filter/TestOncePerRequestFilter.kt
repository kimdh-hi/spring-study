package com.toy.springfilter.filter

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TestOncePerRequestFilter: OncePerRequestFilter() {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    log.info("Called OncePerRequestFilter...")
    filterChain.doFilter(request, response)
  }
}