package com.lecture.inflearnspringsecurityoauth2.config.converter

import com.lecture.inflearnspringsecurityoauth2.model.social.NaverUser
import com.lecture.inflearnspringsecurityoauth2.model.social.SocialType
import com.lecture.inflearnspringsecurityoauth2.utils.OAuth2Utils

class OAuth2NaverProviderUserConverter: ProviderUserConverter<ProviderUserRequest, NaverUser> {

  override fun convert(providerUserRequest: ProviderUserRequest): NaverUser? {
    if(providerUserRequest.clientRegistration?.registrationId != SocialType.NAVER.description) {
      return null
    }

    return NaverUser(
      providerUserRequest.oAuth2User!!,
      OAuth2Utils.getSubAttributes(providerUserRequest.oAuth2User, "response"),
      providerUserRequest.clientRegistration
    )
  }
}