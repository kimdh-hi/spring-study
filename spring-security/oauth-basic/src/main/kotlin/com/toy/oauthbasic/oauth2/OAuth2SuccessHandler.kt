package com.toy.oauthbasic.oauth2

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2SuccessHandler(
  private val jacksonConverter: MappingJackson2HttpMessageConverter
): AuthenticationSuccessHandler {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun onAuthenticationSuccess(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authentication: Authentication
  ) {
    log.info("[OAuth2SuccessHandler] authentication: {}", authentication)
    val principal = authentication.principal as OAuth2UserPrincipal
    val responseVO = OAuth2TokenResponseVO(
      accessToken = principal.oAuth2Token.tokenValue,
      idToken = principal.idToken?.tokenValue
    )
    log.info("[OAuth2SuccessHandler] principal: {}", principal)
    log.info("[OAuth2SuccessHandler] responseVO: {}", responseVO)
    response.status = HttpStatus.OK.value()
    jacksonConverter.write(responseVO, MediaType.APPLICATION_JSON, ServletServerHttpResponse(response))
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