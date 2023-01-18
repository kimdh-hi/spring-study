package com.lecture.userservice.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.lecture.userservice.service.UserService
import com.lecture.userservice.vo.LoginRequestVO
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
  private val userService: UserService,
  private val environment: Environment
): UsernamePasswordAuthenticationFilter() {

  private val log = LoggerFactory.getLogger(javaClass)

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

    val username = (authResult.principal as User).username
    val user = userService.findByUsername(username)

    val secret = environment.getProperty("token.secret")
    log.info("[successfulAuthentication] secret: {}", secret)

    val token = Jwts.builder()
      .setSubject(user.id)
      .setExpiration(Date(System.currentTimeMillis() + environment.getProperty("token.expiration_time")!!.toLong()))
      .signWith(SignatureAlgorithm.HS512, secret)
      .compact()

    response.addHeader("token", token)
    response.addHeader("userId", user.id)
  }
}