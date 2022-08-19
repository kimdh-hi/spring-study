package com.toy.oauthclientoidc.config

import com.toy.oauthclientoidc.auth.HttpCookieOAuth2AuthorizationRequestRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
  private val clientRegistrationRepository: ClientRegistrationRepository,
  private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) {

  @Bean
  fun configure(http: HttpSecurity): SecurityFilterChain {
    return http
      .oauth2Login{ it
        .loginPage("/login")
        .clientRegistrationRepository(clientRegistrationRepository)
        .authorizationEndpoint()
        .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
        .and()
        .userInfoEndpoint()
        .oidcUserService(OidcUserService())
      }
      .build()
  }
}

