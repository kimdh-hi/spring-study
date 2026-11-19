package com.toy.oauth2clientcredentials.domain.repository

import com.toy.oauth2clientcredentials.domain.model.OAuthClient
import org.springframework.data.jpa.repository.JpaRepository

interface OAuthClientRepository : JpaRepository<OAuthClient, Long> {
  fun findByClientId(clientId: String): OAuthClient?

  fun existsByClientId(clientId: String): Boolean
}
