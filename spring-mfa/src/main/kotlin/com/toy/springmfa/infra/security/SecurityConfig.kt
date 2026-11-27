package com.toy.springmfa.infra.security

import com.toy.springmfa.infra.security.jwt.JwtAuthoritiesConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.authorization.AllRequiredFactorsAuthorizationManager
import org.springframework.security.authorization.AuthorizationManagerFactory
import org.springframework.security.authorization.DefaultAuthorizationManagerFactory
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.security.web.webauthn.authentication.WebAuthnAuthenticationProvider
import org.springframework.security.web.webauthn.management.WebAuthnRelyingPartyOperations
import tools.jackson.databind.json.JsonMapper

@Configuration
class SecurityConfig {

  @Bean
  fun securityFilterChain(http: HttpSecurity, jsonMapper: JsonMapper): SecurityFilterChain {
    val mfa = mfaAuthorizationManagerFactory()
    http {
      csrf { disable() }
      sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
      authorizeHttpRequests {
        authorize("/", permitAll)
        authorize("/index.html", permitAll)
        authorize("/login", permitAll)
        authorize("/mfa/passkeys/**", authenticated)
        authorize(anyRequest, mfa.authenticated())
      }
      oauth2ResourceServer {
        jwt { jwtAuthenticationConverter = jwtAuthenticationConverter() }
        accessDeniedHandler = MfaAccessDeniedHandler(jsonMapper)
      }
      exceptionHandling {
        accessDeniedHandler = MfaAccessDeniedHandler(jsonMapper)
      }
    }
    return http.build()
  }

  private fun mfaAuthorizationManagerFactory(): AuthorizationManagerFactory<RequestAuthorizationContext> {
    val passwordAndPasskey = AllRequiredFactorsAuthorizationManager.builder<RequestAuthorizationContext>()
      .requireFactor { it.passwordAuthority() }
      .requireFactor { it.webauthnAuthority() }
      .build()
    return DefaultAuthorizationManagerFactory<RequestAuthorizationContext>().apply {
      setAdditionalAuthorization(passwordAndPasskey)
    }
  }

  private fun jwtAuthenticationConverter() = JwtAuthenticationConverter().apply {
    setJwtGrantedAuthoritiesConverter(JwtAuthoritiesConverter())
  }

  @Bean
  fun authenticationManager(
    userDetailsService: UserDetailsService,
    passwordEncoder: PasswordEncoder,
    webAuthnRelyingPartyOperations: WebAuthnRelyingPartyOperations,
  ): AuthenticationManager =
    ProviderManager(
      DaoAuthenticationProvider(userDetailsService).apply { setPasswordEncoder(passwordEncoder) },
      WebAuthnAuthenticationProvider(webAuthnRelyingPartyOperations, userDetailsService),
    )

  @Bean
  fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
