package com.lecture.inflearnspringsecurityoauth2.model.social

import com.lecture.inflearnspringsecurityoauth2.model.OAuth2ProviderUser
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.user.OAuth2User

class KeycloakUser(
  oAuth2User: OAuth2User,
  clientRegistration: ClientRegistration
): OAuth2ProviderUser(oAuth2User, oAuth2User.attributes, clientRegistration) {
  override fun getId(): String {
    return getAttributes()["sub"].toString()
  }

  override fun getUsername(): String {
    return getAttributes()["preferred_username"].toString()
  }
}