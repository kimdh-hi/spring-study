package com.toy.oauthbasic.oauth2

import com.toy.oauthbasic.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service

@Service
class CustomOidcUserService(
  private val userRepository: UserRepository
): OidcUserService() {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun loadUser(userRequest: OidcUserRequest): OidcUser {
    log.info("[CustomOidcUserService] {}", userRequest)
    val oidcUser = super.loadUser(userRequest)
    val user = userRepository.findByUsername(oidcUser.userInfo.email)?.let {

    }
    return OAuth2UserPrincipal.createOidcUserPrincipal(user, oidcUser, oidcUser.idToken)
  }
}