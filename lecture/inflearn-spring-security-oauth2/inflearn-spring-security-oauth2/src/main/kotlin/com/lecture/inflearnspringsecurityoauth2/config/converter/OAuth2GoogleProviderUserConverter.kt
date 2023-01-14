package com.lecture.inflearnspringsecurityoauth2.config.converter

import com.lecture.inflearnspringsecurityoauth2.model.social.GoogleUser
import com.lecture.inflearnspringsecurityoauth2.model.social.SocialType
import com.lecture.inflearnspringsecurityoauth2.utils.OAuth2Utils

class OAuth2GoogleProviderUserConverter: ProviderUserConverter<ProviderUserRequest, GoogleUser> {

  override fun convert(providerUserRequest: ProviderUserRequest): GoogleUser? {
    if(providerUserRequest.clientRegistration?.registrationId != SocialType.GOOGLE.description) {
      return null
    }

    return GoogleUser(
      providerUserRequest.oAuth2User!!,
      OAuth2Utils.getMainAttributes(providerUserRequest.oAuth2User),
      providerUserRequest.clientRegistration
    )
  }
}