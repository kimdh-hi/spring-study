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

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  val authMethod: ClientAuthMethod,

  val clientSecretHash: String? = null,

  @Column(length = 4096)
  val publicKeyPem: String? = null,

  @Column(nullable = false)
  val createdAt: Instant = Instant.now(),

  @Id @GeneratedValue
  val id: Long? = null,
) {
  init {
    when (authMethod) {
      ClientAuthMethod.SECRET -> require(clientSecretHash != null && publicKeyPem == null)
      ClientAuthMethod.PUBLIC_KEY -> require(publicKeyPem != null && clientSecretHash == null)
    }
  }
}
