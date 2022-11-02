package com.lecture.snsapp.common

import com.lecture.snsapp.exception.ApplicationException
import com.lecture.snsapp.exception.ErrorCode
import com.lecture.snsapp.repository.UserRepository
import com.lecture.snsapp.service.UserService
import com.lecture.snsapp.utils.JwtUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtFilter(
  private val key: String,
  private val userRepository: UserRepository
): OncePerRequestFilter() {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    log.info("[JwtFilter] requestUri: {}", request.requestURI)
    val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
    if (authHeader == null || authHeader.split(" ")[0] != "Bearer"){
      log.warn("[JwtFilter] not exists valid format jwt token")
      filterChain.doFilter(request, response)
      return
    }

    try {
      val token = authHeader.split(" ")[1]
      if(JwtUtils.isExpired(token, key)) {
        log.warn("[JwtFilter] token is expired")
        filterChain.doFilter(request, response)
        return
      }
      val username = JwtUtils.getUsername(token, key)
      val user = userRepository.findByUsername(username) ?: throw ApplicationException(ErrorCode.USER_NOT_FOUND)
      val authentication = UsernamePasswordAuthenticationToken(
        user, null, mutableListOf(SimpleGrantedAuthority(user.role.toString())))
      authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
      SecurityContextHolder.getContext().authentication = authentication
    } catch (ex: RuntimeException) {
      log.error("[JwtFilter] invalid jwt token", ex)
      filterChain.doFilter(request, response)
      return
    }
    filterChain.doFilter(request, response)
  }
}