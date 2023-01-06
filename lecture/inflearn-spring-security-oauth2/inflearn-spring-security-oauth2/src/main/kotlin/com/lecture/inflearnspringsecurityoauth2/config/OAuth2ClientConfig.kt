package com.lecture.inflearnspringsecurityoauth2.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class OAuth2ClientConfig {

  @Bean
  fun webSecurityCustomizer() = WebSecurityCustomizer {
    it.ignoring().antMatchers(
      "/static/js/**", "/static/images/**", "/static/css/**", "/static/scss/**"
    )
  }

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http.authorizeRequests { it
      .antMatchers("/").permitAll()
      .anyRequest().authenticated()
    }
    http.oauth2Login(Customizer.withDefaults())
    http.logout { it.logoutSuccessUrl("/") }

    return http.build()
  }
}