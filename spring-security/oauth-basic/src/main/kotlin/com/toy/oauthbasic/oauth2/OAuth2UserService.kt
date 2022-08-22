package com.toy.oauthbasic.oauth2

import com.toy.oauthbasic.service.UserConnectService
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/*
accessToken or idToken 기반으로 resource server 의 정보를 attributes 형태로 가져옴
 */

@Service
@Transactional(readOnly = true)
class CustomOidcUserService(
  private val userConnectService: UserConnectService
): OidcUserService() {

  private val log = LoggerFactory.getLogger(javaClass)

  @Transactional
  override fun loadUser(userRequest: OidcUserRequest): OidcUser {
    log.info("[CustomOidcUserService] {}", userRequest)
    val oidcUser = super.loadUser(userRequest)
    val user = userConnectService.getUserByOidc(userRequest, oidcUser)
    return OAuth2UserPrincipal.createOidcUserPrincipal(
      user = user,
      oidcUser = oidcUser,
      oidcIdToken = userRequest.idToken,
      oAuth2Token = userRequest.accessToken)
  }
}

@Service
@Transactional(readOnly = true)
class CustomOAuth2UserService(
  private val userConnectService: UserConnectService
): DefaultOAuth2UserService() {

  private val log = LoggerFactory.getLogger(javaClass)

  @Transactional
  override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
    log.info("[CustomOauth2UserService] {}", userRequest)
    val oAuth2User = super.loadUser(userRequest)
    val user = userConnectService.getUserByOAuth2(userRequest, oAuth2User)
    return OAuth2UserPrincipal.createOauth2UserPrincipal(user, oAuth2User, userRequest.accessToken)
  }
}