package com.lecture.inflearnspringsecurityoauth2.model

import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.user.OAuth2User

class NaverUser(
  private val oAuth2User: OAuth2User,
  private val clientRegistration: ClientRegistration
): OAuth2ProviderUser(oAuth2User, oAuth2User.attributes["response"] as Map<String, Any>, clientRegistration) {
  override fun getId(): String {
    return getAttributes()["id"].toString()
  }

  override fun getUsername(): String {
    return getAttributes()["email"].toString()
  }
}