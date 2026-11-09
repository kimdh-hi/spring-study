package com.toy.springexception.filter

import com.toy.springexception.exception.ErrorCodes
import com.toy.springexception.exception.KnownException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ExceptionTriggerFilter : OncePerRequestFilter() {

  override fun shouldNotFilter(request: HttpServletRequest): Boolean =
    !request.requestURI.startsWith("/filters/")

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain,
  ) {
    when (request.requestURI) {
      "/filters/known" -> throw KnownException(ErrorCodes.DATA_NOT_FOUND, "data not found in filter.")
      "/filters/unknown" -> throw IllegalStateException("unknown exception in filter...")
      "/filters/send-error" -> {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
        return
      }
    }
    filterChain.doFilter(request, response)
  }
}
