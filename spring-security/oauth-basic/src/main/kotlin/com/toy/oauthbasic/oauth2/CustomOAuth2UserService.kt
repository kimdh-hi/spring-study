package com.toy.oauthbasic.oauth2

import com.toy.oauthbasic.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
  private val userService: UserService
): DefaultOAuth2UserService() {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
    log.info("[CustomOauth2UserService] {}", userRequest)
    val oAuth2User = super.loadUser(userRequest)
    val user = userService.getUserByOAuth2(userRequest, oAuth2User)
    return OAuth2UserPrincipal.createOauth2UserPrincipal(user, oAuth2User, userRequest.accessToken)
  }
}