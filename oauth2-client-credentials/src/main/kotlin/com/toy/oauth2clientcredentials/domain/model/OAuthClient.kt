package com.toy.oauth2clientcredentials.domain.model

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "oauth_client")
class OAuthClient(
  @Column(nullable = false, unique = true)
  val clientId: String,

  @Column(nullable = false)
  val name: String,

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "oauth_client_scope", joinColumns = [JoinColumn(name = "client_pk")])
  @Column(name = "scope", nullable = false)
  val scopes: Set<String>,

  credential: ClientCredential,

  @Column(nullable = false)
  val createdAt: Instant = Instant.now(),

  @Id @GeneratedValue
  val id: Long? = null,
) {
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private val credentialType: CredentialType

  @Column(nullable = false, length = 4096)
  private val credentialValue: String

  init {
    val (type, value) = when (credential) {
      is ClientCredential.Secret -> CredentialType.SECRET to credential.hash
      is ClientCredential.Pem -> CredentialType.PEM to credential.pem
      is ClientCredential.JwkSetUrl -> CredentialType.JWK_SET_URL to credential.url
    }
    credentialType = type
    credentialValue = value
  }

  val credential: ClientCredential
    get() = when (credentialType) {
      CredentialType.SECRET -> ClientCredential.Secret(credentialValue)
      CredentialType.PEM -> ClientCredential.Pem(credentialValue)
      CredentialType.JWK_SET_URL -> ClientCredential.JwkSetUrl(credentialValue)
    }

  enum class CredentialType { SECRET, PEM, JWK_SET_URL }
}
