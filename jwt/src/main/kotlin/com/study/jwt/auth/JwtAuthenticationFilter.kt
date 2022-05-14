package com.study.jwt.auth

import com.study.jwt.exception.ErrorCodes
import com.study.jwt.exception.JwtAuthenticationException
import com.study.jwt.vo.ErrorResponseVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter (
    private val jwtUtil: JwtUtil,
    filterProcessingUrl: String,
): AbstractAuthenticationProcessingFilter(filterProcessingUrl) {

    @Autowired
    lateinit var jacksonConverter: MappingJackson2HttpMessageConverter

    init {
        setAuthenticationManager { authentication -> authentication }
        setAuthenticationSuccessHandler(JwtAuthenticationSuccessHandler())
    }

    private val bearerPrefix = "bearer "

    /**
     * filterProcessingUrl에 매칭되는 요청은 모두 첫번째로 doFilter 메서드를 타게 된다.
     * 토큰이 존재하지 않다면 인증을 수행하고, Authentication 을 세팅하는 등의 작업을 수행하는 필터를 타지 않는다.
     */
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val token = getToken(httpServletRequest)
        if (token.isNullOrBlank()) {
            chain.doFilter(request, response)
            return
        }

        super.doFilter(request, response, chain)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val token = getToken(request) ?: throw JwtAuthenticationException("token not exists.", ErrorCodes.INVALID_TOKEN)
        val jwtPrincipal = jwtUtil.parseToken(token)
        val authentication = UsernamePasswordAuthenticationToken(jwtPrincipal, null, AuthorityUtils.createAuthorityList(jwtPrincipal.authority))
//        SecurityContextHolder.getContext().authentication = authentication

        return authentication
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        super.successfulAuthentication(request, response, chain, authResult)
        chain.doFilter(request, response)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        val errorResponseVO = ErrorResponseVO(ErrorCodes.INVALID_TOKEN, "unsuccessful Authentication...")
        jacksonConverter.write(errorResponseVO, MediaType.APPLICATION_JSON, ServletServerHttpResponse(response))
        return
    }

    private fun getToken(request: HttpServletRequest): String? {
        return getAuthorizationHeader(request)?.let { bearerToken ->
            extractTokenFromBearer(bearerToken)
        }
    }

    private fun extractTokenFromBearer(bearerToken: String): String {
        return bearerToken.substring(bearerPrefix.length)
    }

    private fun getAuthorizationHeader(request: HttpServletRequest): String? {
        return request.getHeader(HttpHeaders.AUTHORIZATION)
    }
}