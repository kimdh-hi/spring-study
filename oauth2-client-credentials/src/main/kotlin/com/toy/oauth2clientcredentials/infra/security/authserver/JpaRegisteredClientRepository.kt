package com.toy.oauth2clientcredentials.infra.security.authserver

import com.toy.oauth2clientcredentials.domain.model.ClientCredential
import com.toy.oauth2clientcredentials.domain.model.OAuthClient
import com.toy.oauth2clientcredentials.domain.repository.OAuthClientRepository
import com.toy.oauth2clientcredentials.infra.security.SecurityProperties
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
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

  private fun OAuthClient.toRegisteredClient(): RegisteredClient {
    val builder = RegisteredClient.withId(id.toString())
      .clientId(clientId)
      .clientName(name)
      .clientIdIssuedAt(createdAt)
      .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
      .scopes { it.addAll(scopes) }
      .tokenSettings(
        TokenSettings.builder()
          .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
          .accessTokenTimeToLive(properties.clientTokenTtl)
          .build(),
      )

    when (val credential = credential) {
      is ClientCredential.Secret -> builder
        .clientSecret(credential.hash)
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)

      is ClientCredential.PublicKey -> builder
        .clientAuthenticationMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT)
        .clientSettings(privateKeyJwtSettings(credential))
    }
    return builder.build()
  }

  private fun privateKeyJwtSettings(key: ClientCredential.PublicKey): ClientSettings {
    val settings = ClientSettings.builder().tokenEndpointAuthenticationSigningAlgorithm(SignatureAlgorithm.RS256)
    when (key) {
      is ClientCredential.Pem -> settings.setting(ClientAssertionDecoderFactory.PUBLIC_KEY_PEM_SETTING, key.pem)
      is ClientCredential.JwkSetUrl -> settings.jwkSetUrl(key.url)
    }
    return settings.build()
  }
}
