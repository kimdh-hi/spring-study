package com.toy.oauthbasic.oauth2

import org.springframework.http.HttpHeaders
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class OAuth2Filter: OncePerRequestFilter() {

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    val token = request.getHeader(HttpHeaders.AUTHORIZATION)

  }
}