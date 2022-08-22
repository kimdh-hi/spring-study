package com.toy.oauthbasic.config

import com.toy.oauthbasic.oauth2.CustomOAuth2UserService
import com.toy.oauthbasic.oauth2.CustomOidcUserService
import com.toy.oauthbasic.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.toy.oauthbasic.oauth2.OAuth2SuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
class SecurityConfig(
  private val customOauth2UserService: CustomOAuth2UserService,
  private val customOidcUserService: CustomOidcUserService,
  private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
  private val oAuth2SuccessHandler: OAuth2SuccessHandler,
  private val clientRegistrationRepository: ClientRegistrationRepository,
  private val authorizedClientService: OAuth2AuthorizedClientService
) {

  @Bean
  fun configure(http: HttpSecurity): SecurityFilterChain {
    return http
      .oauth2Login { oauth2Login ->
        oauth2Login.authorizedClientService(authorizedClientService)
        oauth2Login.clientRegistrationRepository(clientRegistrationRepository)
        oauth2Login.userInfoEndpoint { it
          .userService(customOauth2UserService)
          .oidcUserService(customOidcUserService)
        }
        oauth2Login.authorizationEndpoint { it
          .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
        }
        oauth2Login.successHandler(oAuth2SuccessHandler)
      }
      .sessionManagement { it
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      }
      .build()
  }
}