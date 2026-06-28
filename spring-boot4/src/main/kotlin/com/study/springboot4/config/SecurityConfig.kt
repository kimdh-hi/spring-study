package com.study.springboot4.config

import com.study.springboot4.filter.ThreadLocalAndSecurityFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .csrf { it.disable() }
      .httpBasic { it.disable() }
      .logout { it.disable() }
      .headers { header ->
        header.frameOptions { it.sameOrigin() }
        header.contentTypeOptions { it.disable() }
      }
      .securityContext { it.disable() }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .authorizeHttpRequests { auth ->
        auth.anyRequest().permitAll()
      }
      .addFilterBefore(ThreadLocalAndSecurityFilter(), UsernamePasswordAuthenticationFilter::class.java)
    return http.build()
  }
}
