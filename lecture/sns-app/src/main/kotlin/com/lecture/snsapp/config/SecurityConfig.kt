package com.lecture.snsapp.config

import com.lecture.snsapp.common.JwtFilter
import com.lecture.snsapp.common.Response
import com.lecture.snsapp.exception.ErrorCode
import com.lecture.snsapp.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
class SecurityConfig(
  private val userRepository: UserRepository
) {

  @Value("\${jwt.secret-key}") lateinit var secretKey: String

  @Bean
  fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

  @Bean
  fun webIgnored() = WebSecurityCustomizer {
    // /api 로 시작하는 요청이 아닌 모든 요청
    // /api 로 시작하는 요청이 불필요하게 security filter chain 을 타지 않도록 무시한다.
    it.ignoring()
      .regexMatchers("^(?!/api/).*")
  }

  @Bean
  @Order(0)
  fun ignoredPatternFilterChain(http: HttpSecurity): SecurityFilterChain =
  http
  .requestMatchers { it
    .requestMatchers(
      PathRequest.toStaticResources().atCommonLocations(),
    )
  }
  .authorizeHttpRequests { authorize -> authorize.anyRequest().permitAll() }
  .requestCache().disable()
  .securityContext().disable()
  .sessionManagement().disable()
  .build()

  @Bean
  fun configure(http: HttpSecurity): SecurityFilterChain =
    http
      .csrf { it.disable() }
      .authorizeRequests { it
        .antMatchers("/api/*/users/join", "/api/*/users/login", "/api/*/users/**").permitAll()
        .antMatchers("/api/**").authenticated()
      }
      .sessionManagement { it
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      }
      .addFilterBefore(
        JwtFilter(secretKey, userRepository),
        UsernamePasswordAuthenticationFilter::class.java
      )
      .exceptionHandling()
      .authenticationEntryPoint(CustomAuthenticationEntryPoint())
      .and()
      .build()
}

class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {
  override fun commence(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authException: AuthenticationException
  ) {
    response.contentType = MediaType.APPLICATION_JSON_VALUE
    response.status = HttpStatus.UNAUTHORIZED.value()
    response.writer.write(
      Response.error(ErrorCode.INVALID_AUTHENTICATION.errorCode).toHttpResponse()
    )
  }
}