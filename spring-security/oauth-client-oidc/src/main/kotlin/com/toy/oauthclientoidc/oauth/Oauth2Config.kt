package com.toy.oauthclientoidc.oauth

import com.toy.oauthclientoidc.common.Oauth2Properties
import com.toy.oauthclientoidc.common.Oauth2Resource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository

@Configuration
class Oauth2Config(
  private val oauth2Properties: Oauth2Properties
) {

  @Bean
  fun clientRegistrationRepository(): ClientRegistrationRepository {

    val registrations = oauth2Properties.registration
      .entries
      .stream()
      .map { getClientRegistration(it.value) }
      .toList()

    return InMemoryClientRegistrationRepository(registrations)
  }

  private fun getClientRegistration(resource: Oauth2Resource): ClientRegistration {
    return CustomOAuth2Provider.caseInsensitiveValueOf(resource.clientName)
      .getBuilder(resource.clientName)!!
      .clientId(resource.clientId)
      .clientSecret(resource.clientSecret)
      .build()
  }

}