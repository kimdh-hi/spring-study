package com.lecture.inflearnspringsecurityoauth2.service

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>, AbstractOAuth2UserService() {

  override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
    val clientRegistration = userRequest.clientRegistration
    val oAuth2UserService = DefaultOAuth2UserService()
    val oAuth2User = oAuth2UserService.loadUser(userRequest)

    val providerUser = super.providerUser(clientRegistration, oAuth2User)
    super.register(providerUser, userRequest)
  }
}