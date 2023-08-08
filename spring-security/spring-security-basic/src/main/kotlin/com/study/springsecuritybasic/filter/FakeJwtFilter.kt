package com.study.springsecuritybasic.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.filter.OncePerRequestFilter

//필터를 bean 으로 등록시 명시하지 않은 모든 securityFilterChain 에 필터가 추가됨
class FakeJwtFilter(
  private val jacksonConverter: MappingJackson2HttpMessageConverter
): OncePerRequestFilter() {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    log.info("FakeJwtFilter called...")

    val token = request.getHeader("MyToken")
    if (token.isNullOrBlank() || token != "myToken") {
      val unauthorized = HttpServletResponse.SC_UNAUTHORIZED
      response.status = unauthorized
      val responseVO = ErrorResponseVO.error(unauthorized.toString())
      jacksonConverter.write(responseVO, MediaType.APPLICATION_JSON, ServletServerHttpResponse(response))
    }

    filterChain.doFilter(request, response)
  }
}

data class ErrorResponseVO(
  var errorCode: String,
  var message: Any?,
) {

  companion object {
    fun error(errorCode: String, message: Any? = null): ErrorResponseVO {
      return ErrorResponseVO(errorCode, message ?: "Unknown exception..")
    }
  }

}