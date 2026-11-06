package com.toy.dpopproofjwt.infra.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

class DPoPNonceFilter(
  private val nonceProvider: DPoPNonceProvider,
) : OncePerRequestFilter() {

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    response.setHeader(DPOP_NONCE_HEADER, nonceProvider.issue())
    filterChain.doFilter(request, response)
  }
}
