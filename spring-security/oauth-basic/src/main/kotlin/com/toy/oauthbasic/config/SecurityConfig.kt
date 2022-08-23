package com.toy.oauthbasic.config

import com.toy.oauthbasic.oauth2.*
import com.toy.oauthbasic.utils.JwtUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
class SecurityConfig(
  private val customOauth2UserService: CustomOAuth2UserService,
  private val customOidcUserService: CustomOidcUserService,
  private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
  private val oAuth2SuccessHandler: OAuth2SuccessHandler,
  private val oAuth2FailureHandler: OAuth2FailureHandler,
  private val clientRegistrationRepository: ClientRegistrationRepository,
  private val authorizedClientService: OAuth2AuthorizedClientService,
  private val jwtUtils: JwtUtils
) {

  @Bean
  fun jwtAuthenticationFilter() = JwtAuthenticationFilter(jwtUtils)

  @Bean
  fun webSecurityCustomizer(): WebSecurityCustomizer {
    return WebSecurityCustomizer { web: WebSecurity -> web.ignoring().antMatchers("/h2-console/**") }
  }

  @Bean
  fun configure(http: HttpSecurity): SecurityFilterChain {
    return http
      .authorizeHttpRequests { it
        .antMatchers("/login", "/hello").permitAll()
        .antMatchers("/auth").authenticated()
      }
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
        oauth2Login.failureHandler(oAuth2FailureHandler)
      }
      .sessionManagement { it
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      }
      .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
      .build()
  }
}