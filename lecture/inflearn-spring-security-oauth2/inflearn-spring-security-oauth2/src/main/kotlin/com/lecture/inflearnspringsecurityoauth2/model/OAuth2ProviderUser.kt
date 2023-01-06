package com.lecture.inflearnspringsecurityoauth2.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.UUID

abstract class OAuth2ProviderUser(
  private val oAuth2User: OAuth2User,
  private val attributes: Map<String, Any>,
  private val clientRegistration: ClientRegistration
): ProviderUser {

  override fun getEmail(): String = attributes["email"].toString()

  override fun getProvider(): String = clientRegistration.registrationId

  override fun getPassword(): String = UUID.randomUUID().toString()

  override fun getAuthorities(): List<GrantedAuthority> = oAuth2User.authorities
    .map { SimpleGrantedAuthority(it.authority) }
  override fun getAttributes(): Map<String, Any> = attributes
}