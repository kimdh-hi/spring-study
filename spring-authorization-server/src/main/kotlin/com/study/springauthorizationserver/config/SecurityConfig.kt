package com.study.springauthorizationserver.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
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

@Configuration
class SecurityConfig {

  @Bean
  fun filterChain(
    http: HttpSecurity,
    registeredClientRepository: RegisteredClientRepository,
    authorizationService: OAuth2AuthorizationService,
    authorizationServerSettings: AuthorizationServerSettings,
    tokenGenerator: OAuth2TokenGenerator<OAuth2Token>,
  ): SecurityFilterChain {
    val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer()

    http
      .securityMatcher(authorizationServerConfigurer.endpointsMatcher)
      .with(authorizationServerConfigurer) {
        it.registeredClientRepository(registeredClientRepository)
        it.authorizationService(authorizationService)
        it.authorizationServerSettings(authorizationServerSettings)
        it.tokenGenerator(tokenGenerator)
      }
      .csrf { it.disable() }
      .authorizeHttpRequests { it.anyRequest().authenticated() }

    return http.build()
  }

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
    return InMemoryRegisteredClientRepository(registeredClient)
  }

  @Bean
  fun authorizationService(): OAuth2AuthorizationService = InMemoryOAuth2AuthorizationService()

  @Bean
  fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder = OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)

  @Bean
  fun jwtEncoder(jwkSource: JWKSource<SecurityContext>): JwtEncoder = NimbusJwtEncoder(jwkSource)

  @Bean
  fun authorizationServerSettings(): AuthorizationServerSettings = AuthorizationServerSettings.builder()
    .issuer("http://localhost:8080")
    .tokenEndpoint("/v1/oauth2/token").build() // default: oauth2/token

  @Bean
  fun tokenGenerator(jwtEncoder: JwtEncoder): OAuth2TokenGenerator<OAuth2Token> {
    val jwtGenerator = JwtGenerator(jwtEncoder)
    return DelegatingOAuth2TokenGenerator(jwtGenerator)
  }

  @Bean
  fun jwkSource(): JWKSource<SecurityContext> {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048)
    val keyPair = keyPairGenerator.generateKeyPair()
    val rsaKey = RSAKey.Builder(keyPair.public as RSAPublicKey)
      .privateKey(keyPair.private as RSAPrivateKey)
      .keyID(UUID.randomUUID().toString())
      .build()
    return ImmutableJWKSet(JWKSet(rsaKey))
  }

}
