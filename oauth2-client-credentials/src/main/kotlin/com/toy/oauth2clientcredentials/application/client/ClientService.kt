package com.toy.oauth2clientcredentials.application.client

import com.toy.oauth2clientcredentials.common.exception.InvalidClientMetadataException
import com.toy.oauth2clientcredentials.common.exception.InvalidScopeException
import com.toy.oauth2clientcredentials.common.exception.NotFoundException
import com.toy.oauth2clientcredentials.domain.model.ApiScope
import com.toy.oauth2clientcredentials.domain.model.ClientAuthMethod
import com.toy.oauth2clientcredentials.domain.model.OAuthClient
import com.toy.oauth2clientcredentials.domain.repository.OAuthClientRepository
import com.toy.oauth2clientcredentials.infra.security.authserver.RsaPublicKeys
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.time.Instant
import java.util.Base64
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ClientService(
  private val clientRepository: OAuthClientRepository,
  private val passwordEncoder: PasswordEncoder,
) {
  private val secureRandom = SecureRandom()

  @Transactional
  fun create(command: CreateClientCommand): CreatedClient {
    require(command.scopes.isNotEmpty()) { "scopes must not be empty" }
    val unsupported = command.scopes.filterNot(ApiScope::isSupported)
    if (unsupported.isNotEmpty()) throw InvalidScopeException("unsupported scopes: $unsupported")

    val (clientSecret, publicKeyPem) = when (command.authMethod) {
      ClientAuthMethod.SECRET -> newSecret() to null
      ClientAuthMethod.PUBLIC_KEY -> null to validPublicKey(command.publicKey)
    }
    val client = clientRepository.save(
      OAuthClient(
        clientId = UUID.randomUUID().toString().replace("-", ""),
        name = command.name,
        scopes = command.scopes,
        authMethod = command.authMethod,
        clientSecretHash = clientSecret?.let { passwordEncoder.encode(it) },
        publicKeyPem = publicKeyPem,
      ),
    )
    return CreatedClient(ClientInfo.from(client), clientSecret)
  }

  fun findAll(): List<ClientInfo> = clientRepository.findAll().map(ClientInfo::from)

  fun find(clientId: String): ClientInfo = ClientInfo.from(getClient(clientId))

  @Transactional
  fun delete(clientId: String) = clientRepository.delete(getClient(clientId))

  private fun getClient(clientId: String) =
    clientRepository.findByClientId(clientId) ?: throw NotFoundException("client not found: $clientId")

  private fun newSecret(): String {
    val bytes = ByteArray(32).also(secureRandom::nextBytes)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
  }

  private fun validPublicKey(pem: String?): String {
    if (pem == null || runCatching { RsaPublicKeys.parse(pem) }.isFailure) {
      throw InvalidClientMetadataException("valid RSA publicKey (X.509 PEM) is required")
    }
    return pem
  }
}

data class CreateClientCommand(
  val name: String,
  val scopes: Set<String>,
  val authMethod: ClientAuthMethod = ClientAuthMethod.SECRET,
  val publicKey: String? = null,
)

data class ClientInfo(
  val clientId: String,
  val name: String,
  val scopes: Set<String>,
  val authMethod: ClientAuthMethod,
  val createdAt: Instant,
) {
  companion object {
    fun from(client: OAuthClient) = ClientInfo(
      clientId = client.clientId,
      name = client.name,
      scopes = client.scopes.toSortedSet(),
      authMethod = client.authMethod,
      createdAt = client.createdAt,
    )
  }
}

data class CreatedClient(
  val client: ClientInfo,
  val clientSecret: String?,
)
