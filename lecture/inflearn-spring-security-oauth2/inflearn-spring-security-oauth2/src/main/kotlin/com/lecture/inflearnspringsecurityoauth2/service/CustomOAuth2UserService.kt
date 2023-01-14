package com.lecture.inflearnspringsecurityoauth2.service

import com.lecture.inflearnspringsecurityoauth2.config.converter.ProviderUserRequest
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>, AbstractOAuth2UserService() {

  override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
    val clientRegistration = userRequest.clientRegistration
    val userService = DefaultOAuth2UserService()
    val oAuth2User = userService.loadUser(userRequest)

    val providerUserRequest = ProviderUserRequest(clientRegistration = clientRegistration, oAuth2User = oAuth2User)

    val providerUser = providerUser(providerUserRequest)

    super.register(providerUser, userRequest)

    return oAuth2User
  }
}