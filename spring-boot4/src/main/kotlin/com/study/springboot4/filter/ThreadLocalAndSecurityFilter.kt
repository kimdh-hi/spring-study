package com.study.springboot4.filter

import com.study.springboot4.context.RequestContextHolder
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

class ThreadLocalAndSecurityFilter : OncePerRequestFilter() {
  private val log = LoggerFactory.getLogger(ThreadLocalAndSecurityFilter::class.java)

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    try {
      val requestId = UUID.randomUUID().toString()
      val userInfo = "User-${System.currentTimeMillis()}"

      RequestContextHolder.setRequestId(requestId)
      RequestContextHolder.setUserInfo(userInfo)

      val authentication = UsernamePasswordAuthenticationToken(
        "testUser",
        "password",
        listOf(SimpleGrantedAuthority("ROLE_USER"))
      )
      SecurityContextHolder.getContext().authentication = authentication

      log.info(
        "[Filter] ThreadId={}, VirtualThread={}, RequestId={}, UserInfo={}, SecurityUser={}",
        Thread.currentThread().threadId(),
        Thread.currentThread().isVirtual,
        RequestContextHolder.getRequestId(),
        RequestContextHolder.getUserInfo(),
        SecurityContextHolder.getContext().authentication?.name
      )

      filterChain.doFilter(request, response)
    } finally {
      RequestContextHolder.clear()
      SecurityContextHolder.clearContext()
    }
  }
}
