package com.toy.oauthclientoidc.config

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
  private val clientRegistrationRepository: ClientRegistrationRepository
) {

  @Bean
  fun configure(http: HttpSecurity): SecurityFilterChain {
    return http
      .oauth2Login{ it
        .clientRegistrationRepository(clientRegistrationRepository)
          .userInfoEndpoint()
          .oidcUserService(OidcUserService())
      }
      .build()
  }
}

