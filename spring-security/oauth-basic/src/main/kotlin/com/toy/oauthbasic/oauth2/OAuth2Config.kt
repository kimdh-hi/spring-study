package com.toy.oauthbasic.oauth2

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository

@Configuration
class OAuth2Config(
  private val oAuth2Properties: OAuth2Properties
) {

  //todo, oauth2 인증정보를 in-memory 가 아닌 db에 저장하는 방식 (in-memory보다 나은지 확인)
  @Bean
  fun authorizedClientService(jdbcTemplate: JdbcTemplate): OAuth2AuthorizedClientService {
    return JdbcOAuth2AuthorizedClientService(jdbcTemplate, clientRegistrationRepository())
  }

//  @Bean
//  fun authorizedClientService(): OAuth2AuthorizedClientService {
//    return InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository())
//  }

  @Bean
  fun clientRegistrationRepository(): ClientRegistrationRepository {

    val registrations = oAuth2Properties.registration
      .entries
      .stream()
      .map { getClientRegistration(it.value) }
      .toList()

    return InMemoryClientRegistrationRepository(registrations)
  }

  private fun getClientRegistration(resource: OAuth2Resource): ClientRegistration {
    return Idp.caseInsensitiveValueOf(resource.clientName)
      .getBuilder(resource.clientName)!!
      .clientId(resource.clientId)
      .clientSecret(resource.clientSecret)
      .build()
  }
}