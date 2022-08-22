package com.toy.oauthbasic.oauth2

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository

@Configuration
class OAuth2Config(
  private val oAuth2Properties: OAuth2Properties
) {

//  @Bean
//  fun authorizedClientService(jdbcTemplate: JdbcTemplate): OAuth2AuthorizedClientService {
//    return JdbcOAuth2AuthorizedClientService(jdbcTemplate, clientRegistrationRepository())
//  }

  @Bean
  fun authorizedClientService(): OAuth2AuthorizedClientService {
    return InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository())
  }

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