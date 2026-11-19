package com.toy.oauth2clientcredentials.infra.security

import com.toy.oauth2clientcredentials.domain.model.OAuthClient
import com.toy.oauth2clientcredentials.domain.repository.OAuthClientRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class JpaRegisteredClientRepository(
  private val clientRepository: OAuthClientRepository,
  private val properties: SecurityProperties,
) : RegisteredClientRepository {

  override fun save(registeredClient: RegisteredClient) =
    throw UnsupportedOperationException("register clients via admin api")

  override fun findById(id: String): RegisteredClient? =
    id.toLongOrNull()?.let { clientRepository.findById(it).orElse(null) }?.toRegisteredClient()

  override fun findByClientId(clientId: String): RegisteredClient? =
    clientRepository.findByClientId(clientId)?.toRegisteredClient()

  private fun OAuthClient.toRegisteredClient(): RegisteredClient =
    RegisteredClient.withId(id.toString())
      .clientId(clientId)
      .clientSecret(clientSecretHash)
      .clientName(name)
      .clientIdIssuedAt(createdAt)
      .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
      .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
      .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
      .scopes { it.addAll(scopes) }
      .tokenSettings(
        TokenSettings.builder()
          .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
          .accessTokenTimeToLive(properties.clientTokenTtl)
          .build(),
      )
      .build()
}
