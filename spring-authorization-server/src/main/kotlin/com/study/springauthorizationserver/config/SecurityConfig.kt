package com.study.springauthorizationserver.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator
import org.springframework.security.web.SecurityFilterChain
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Duration
import java.util.*

//client credential grant configuration sample
@Configuration
class SecurityConfig {

  @Bean
  fun filterChain(
    http: HttpSecurity,
    registeredClientRepository: RegisteredClientRepository,
    authorizationServerSettings: AuthorizationServerSettings,
    tokenGenerator: OAuth2TokenGenerator<OAuth2Token>,
  ): SecurityFilterChain {
    val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()

    http
      .securityMatcher(authorizationServerConfigurer.endpointsMatcher)
      .with(authorizationServerConfigurer) { it
        .registeredClientRepository(registeredClientRepository)
        .authorizationServerSettings(authorizationServerSettings)
        .tokenGenerator(tokenGenerator)
      }
      .csrf { it.disable() }
      .authorizeHttpRequests { it.anyRequest().authenticated() }

    return http.build()
  }

  /**
   * JdbcRegisteredClientRepository
   * - 벌도 스키마 생성 필요
   * - 구현체 제공되므로 설정 용이
   *
   * RegisteredClientRepository 구현체 직접 정의하는 경우 redis 등 다른 저장소 사용 가능
   */
  @Bean
  fun registeredClientRepository(): RegisteredClientRepository {
    val registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
      .clientName("test client name")
      .clientId("testClientId")
      .clientSecret("{noop}testSecret")
      .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
      .clientSettings(ClientSettings.builder().build())
      .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(2)).build())
      .build()

    //for test
    return InMemoryRegisteredClientRepository(registeredClient)
  }

  @Bean
  fun authorizationServerSettings(): AuthorizationServerSettings = AuthorizationServerSettings.builder()
    .issuer("http://localhost:8080")
    .tokenEndpoint("/v1/oauth2/token").build() // default: oauth2/token

  @Bean
  fun jwtEncoder(jwkSource: JWKSource<SecurityContext>): JwtEncoder = NimbusJwtEncoder(jwkSource)

  //accessToken create (/oauth2/token)
  @Bean
  fun tokenGenerator(jwtEncoder: JwtEncoder): OAuth2TokenGenerator<OAuth2Token> {
    val jwtGenerator = JwtGenerator(jwtEncoder)
    return DelegatingOAuth2TokenGenerator(jwtGenerator)
  }

  //for test
  //운영시 별도 key 관리 파일 또는 툴(aws secret manager..) 등 적용 필요
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
  @Order(2)
  fun resourceServerFilterChain(http: HttpSecurity, jwkSource: JWKSource<SecurityContext>): SecurityFilterChain {
    http
      .securityMatcher("/api/**")
      .authorizeHttpRequests { it.anyRequest().authenticated() }
      .oauth2ResourceServer { oauth2 ->
        oauth2.jwt { jwt -> jwt.decoder(OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)) }
      }

    return http.build()
  }
}
