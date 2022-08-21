package com.toy.oauthbasic.oauth2

import com.toy.oauthbasic.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service

@Service
class CustomOidcUserService(
  private val userService: UserService
): OidcUserService() {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun loadUser(userRequest: OidcUserRequest): OidcUser {
    log.info("[CustomOidcUserService] {}", userRequest)
    val oidcUser = super.loadUser(userRequest)
    val user = userService.getUserByOAuth2(userRequest, oidcUser)
    return OAuth2UserPrincipal.createOidcUserPrincipal(
      user = user,
      oidcUser = oidcUser,
      oidcIdToken = userRequest.idToken,
      oAuth2Token = userRequest.accessToken)
  }
}