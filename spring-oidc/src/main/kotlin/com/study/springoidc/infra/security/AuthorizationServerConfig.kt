package com.study.springoidc.infra.security

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import com.study.springoidc.application.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Duration
import java.util.UUID

@Configuration
class AuthorizationServerConfig {

  // order 1: /oauth2/**, /.well-known/** 등 인가 서버 엔드포인트 전용 체인
  @Bean
  @Order(1)
  fun authorizationServerFilterChain(http: HttpSecurity): SecurityFilterChain {
    val authorizationServer = OAuth2AuthorizationServerConfigurer()

    http
      // endpointsMatcher 로 인가 서버 엔드포인트만 이 체인이 처리
      .securityMatcher(authorizationServer.endpointsMatcher)
      // oidc 활성화 -> userinfo, id_token 지원
      .with(authorizationServer) { it.oidc(Customizer.withDefaults()) }
      .authorizeHttpRequests { it.anyRequest().authenticated() }
      // 토큰 엔드포인트는 클라이언트 인증 기반이라 CSRF 제외
      .csrf { it.ignoringRequestMatchers(authorizationServer.endpointsMatcher) }
      .exceptionHandling {
        // 브라우저(text/html) 요청만 로그인 페이지로 리다이렉트, API 는 401
        it.defaultAuthenticationEntryPointFor(
          LoginUrlAuthenticationEntryPoint("/login"),
          MediaTypeRequestMatcher(MediaType.TEXT_HTML),
        )
      }
      // /userinfo 등 인가 서버 자체 엔드포인트의 access token 검증용
      .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }

    return http.build()
  }

  @Bean
  fun registeredClientRepository(): RegisteredClientRepository {
    val oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
      .clientId("oidc-client")
      .clientSecret("{noop}oidc-secret")
      .clientName("OIDC Sample Client")
      .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
      .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
      .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
      .redirectUri("http://127.0.0.1:9000/authorized")
      .postLogoutRedirectUri("http://127.0.0.1:9000/")
      .scope(OidcScopes.OPENID)
      .scope(OidcScopes.PROFILE)
      .scope(OidcScopes.EMAIL)
      .scope("read")
      .clientSettings(
        ClientSettings.builder()
          .requireAuthorizationConsent(true)
          .requireProofKey(false)
          .build()
      )
      .tokenSettings(
        TokenSettings.builder()
          .accessTokenTimeToLive(Duration.ofMinutes(30))
          .refreshTokenTimeToLive(Duration.ofDays(1))
          .reuseRefreshTokens(false)
          .build()
      )
      .build()

    val machineClient = RegisteredClient.withId(UUID.randomUUID().toString())
      .clientId("service-client")
      .clientSecret("{noop}service-secret")
      .clientName("Machine To Machine Client")
      .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
      .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
      .scope("read")
      .build()

    return InMemoryRegisteredClientRepository(oidcClient, machineClient)
  }

  @Bean
  fun authorizationServerSettings(@Value("\${oidc.issuer}") issuer: String): AuthorizationServerSettings =
    AuthorizationServerSettings.builder()
      .issuer(issuer)
      .build()

  @Bean
  fun jwkSource(): JWKSource<SecurityContext> {
    val keyPair = KeyPairGenerator.getInstance("RSA")
      .apply { initialize(2048) }
      .generateKeyPair()

    val rsaKey = RSAKey.Builder(keyPair.public as RSAPublicKey)
      .privateKey(keyPair.private as RSAPrivateKey)
      .keyID(UUID.randomUUID().toString())
      .build()

    return ImmutableJWKSet(JWKSet(rsaKey))
  }

  @Bean
  fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder =
    OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)

  @Bean
  fun jwtTokenCustomizer(userService: UserService): OAuth2TokenCustomizer<JwtEncodingContext> =
    OAuth2TokenCustomizer { context ->
      if (context.authorizationGrantType == AuthorizationGrantType.CLIENT_CREDENTIALS) {
        return@OAuth2TokenCustomizer
      }

      val user = userService.getByUsername(context.getPrincipal<Authentication>()!!.name)
      when (context.tokenType.value) {
        OidcParameterNames.ID_TOKEN -> context.claims
          .claim("email", user.email)
          .claim("nickname", user.nickname)

        OAuth2TokenType.ACCESS_TOKEN.value -> context.claims
          .claim("roles", user.roleList)
      }
    }
}
