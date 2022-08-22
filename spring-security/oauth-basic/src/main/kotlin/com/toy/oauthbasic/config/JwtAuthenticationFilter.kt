package com.toy.oauthbasic.config

import com.nimbusds.oauth2.sdk.util.StringUtils.isBlank
import com.toy.oauthbasic.exception.JwtAuthenticationException
import com.toy.oauthbasic.exception.JwtExpiredException
import com.toy.oauthbasic.utils.JwtUtils
import com.toy.oauthbasic.vo.ErrorResponseVO
import org.apache.commons.lang3.StringUtils.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
  private val jwtUtils: JwtUtils
): OncePerRequestFilter() {

  @Autowired
  lateinit var jacksonConverter: MappingJackson2HttpMessageConverter

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    val JWT_FILTER_IGNORE_URLS = listOf<String>()
  }

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    log.debug("TokenAuthenticationFilter-> requestUri:{}", request.requestURI)

    val token = getToken(request)

    if (checkSkipAuthentication(token, request)) {
      filterChain.doFilter(request, response)
      return
    }

    try {
      val jwtPrincipal = jwtUtils.parseToken(token!!)
      val authentication = UsernamePasswordAuthenticationToken(jwtPrincipal, null, AuthorityUtils.NO_AUTHORITIES)
      SecurityContextHolder.getContext().authentication = authentication
      filterChain.doFilter(request, response)
    } catch (e: JwtAuthenticationException) {
      log.error("received invalid token", e)
      unsuccessfulAuthentication(response, e)
    } catch (e: JwtExpiredException) {
      log.error("received expired token", e)
      unsuccessfulAuthentication(response, e)
    }

  }

  private fun getToken(request: HttpServletRequest): String? = request.getHeader(HttpHeaders.AUTHORIZATION)

  private fun checkSkipAuthentication(token: String?, request: HttpServletRequest): Boolean = checkToken(token) || checkSkipRequestUri(request)

  private fun checkToken(token: String?): Boolean = token == null || org.apache.commons.lang3.StringUtils.isBlank(token)

  private fun checkSkipRequestUri(request: HttpServletRequest): Boolean = JWT_FILTER_IGNORE_URLS.contains(request.requestURI)

  private fun unsuccessfulAuthentication(response: HttpServletResponse, e: AuthenticationException) {
    val unauthorized = HttpServletResponse.SC_UNAUTHORIZED
    response.status = unauthorized
    val responseVO = ErrorResponseVO.error(unauthorized.toString(), e.message)
    jacksonConverter.write(responseVO, MediaType.APPLICATION_JSON, ServletServerHttpResponse(response))
  }

}