package com.lecture.userservice.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.lecture.userservice.vo.LoginRequestVO
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter: UsernamePasswordAuthenticationFilter() {

  override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
    val requestVO = ObjectMapper().readValue(request.inputStream, LoginRequestVO::class.java)

    return authenticationManager.authenticate(
      UsernamePasswordAuthenticationToken(requestVO.email, requestVO.password, arrayListOf())
    )
  }

  override fun successfulAuthentication(
    request: HttpServletRequest,
    response: HttpServletResponse,
    chain: FilterChain,
    authResult: Authentication,
  ) {
    super.successfulAuthentication(request, response, chain, authResult)
  }
}