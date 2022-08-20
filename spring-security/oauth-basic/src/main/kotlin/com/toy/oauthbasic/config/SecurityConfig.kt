package com.toy.oauthbasic.config

import com.toy.oauthbasic.oauth2.CustomOAuth2UserService
import com.toy.oauthbasic.oauth2.CustomOidcUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
class SecurityConfig(
  private val customOauth2UserService: CustomOAuth2UserService,
  private val customOidcUserService: CustomOidcUserService
) {

  @Bean
  fun configure(http: HttpSecurity): SecurityFilterChain {
    return http
      .oauth2Login { oauth2Login ->
        oauth2Login.userInfoEndpoint { it
          .userService(customOauth2UserService)
          .oidcUserService(customOidcUserService)
        }
      }
      .sessionManagement { it
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      }
      .build()
  }
}