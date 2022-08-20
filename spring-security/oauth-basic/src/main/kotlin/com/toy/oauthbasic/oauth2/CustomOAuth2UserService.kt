package com.toy.oauthbasic.oauth2

import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService: DefaultOAuth2UserService() {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
    log.info("[CustomOauth2UserService] {}", userRequest)
    return super.loadUser(userRequest)
  }
}