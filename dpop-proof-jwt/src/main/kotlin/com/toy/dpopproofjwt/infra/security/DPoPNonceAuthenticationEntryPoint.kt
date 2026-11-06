package com.toy.dpopproofjwt.infra.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.server.resource.web.DPoPAuthenticationEntryPoint
import org.springframework.security.web.AuthenticationEntryPoint

class DPoPNonceAuthenticationEntryPoint : AuthenticationEntryPoint {

  private val delegate = DPoPAuthenticationEntryPoint()

  override fun commence(request: HttpServletRequest, response: HttpServletResponse, ex: AuthenticationException) {
    if (!ex.cause.isUseDPoPNonce()) {
      delegate.commence(request, response, ex)
      return
    }

    response.status = HttpStatus.UNAUTHORIZED.value()
    response.setHeader(
      HttpHeaders.WWW_AUTHENTICATE,
      """DPoP error="$USE_DPOP_NONCE", error_description="Resource server requires nonce in DPoP proof"""",
    )
  }
}
