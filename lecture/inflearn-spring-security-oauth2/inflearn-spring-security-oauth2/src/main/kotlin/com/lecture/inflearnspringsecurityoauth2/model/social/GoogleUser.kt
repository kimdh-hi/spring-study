package com.lecture.inflearnspringsecurityoauth2.model.social

import com.lecture.inflearnspringsecurityoauth2.model.OAuth2ProviderUser
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.user.OAuth2User

class GoogleUser(
  private val oAuth2User: OAuth2User,
  private val attributes: Attributes,
  private val clientRegistration: ClientRegistration
): OAuth2ProviderUser(oAuth2User, attributes.mainAttributes, clientRegistration) {
  override fun getId(): String {
    return getAttributes()["sub"].toString()
  }

  override fun getUsername(): String {
    return getAttributes()["sub"].toString()
  }
}