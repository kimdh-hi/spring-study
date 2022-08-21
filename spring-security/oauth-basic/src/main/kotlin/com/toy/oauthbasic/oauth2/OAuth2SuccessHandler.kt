package com.toy.oauthbasic.oauth2

import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class OAuth2SuccessHandler: AuthenticationSuccessHandler {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun onAuthenticationSuccess(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication
  ) {
    log.info("[OAuth2SuccessHandler] authentication: {}", authentication)
  }
}

class OAuth2FailureHandler: AuthenticationFailureHandler {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun onAuthenticationFailure(
    request: HttpServletRequest,
    response: HttpServletResponse,
    exception: AuthenticationException
  ) {
    log.info("[OAuth2FailureHandler] exception: {}", exception.message)
  }
}