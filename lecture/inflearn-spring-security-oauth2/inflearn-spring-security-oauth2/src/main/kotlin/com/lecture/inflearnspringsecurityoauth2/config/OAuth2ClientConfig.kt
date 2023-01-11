package com.lecture.inflearnspringsecurityoauth2.config

import com.lecture.inflearnspringsecurityoauth2.service.CustomOAuth2UserService
import com.lecture.inflearnspringsecurityoauth2.service.CustomOidcUserService
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class OAuth2ClientConfig(
  private val customOAuth2UserService: CustomOAuth2UserService,
  private val customOidcUserService: CustomOidcUserService
) {

  @Bean
  fun webSecurityCustomizer() = WebSecurityCustomizer {
    it.ignoring().antMatchers(
      "/static/js/**", "/static/images/**", "/static/css/**", "/static/scss/**"
    )
  }

  @Bean
  @Order(0)
  fun ignoredPatternFilterChain(http: HttpSecurity): SecurityFilterChain =
    http
      .requestMatchers { it
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
      }
      .authorizeHttpRequests { authorize -> authorize.anyRequest().permitAll() }
      .requestCache().disable()
      .securityContext().disable()
      .sessionManagement().disable()
      .build()

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http.authorizeRequests { it
      .antMatchers("/api/users").access("hasAnyRole('SCOPE_profile', 'SCOPE_email')")
      .antMatchers("/api/oidc").access("hasAnyRole('SCOPE_openid')")
      .antMatchers("/").permitAll()
      .anyRequest().authenticated()
    }
    http.oauth2Login { oauth2Login ->
      oauth2Login.userInfoEndpoint { userInfoEndPoint ->
        userInfoEndPoint
          .userService(customOAuth2UserService)
          .oidcUserService(customOidcUserService)
      }
    }
    http.logout { it.logoutSuccessUrl("/") }

    return http.build()
  }

  @Bean
  fun customAuthorityMapper(): GrantedAuthoritiesMapper {
    return CustomAuthorityMapper()
  }
}