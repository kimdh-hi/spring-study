package com.lecture.inflearnspringsecurityoauth2.service

import com.lecture.inflearnspringsecurityoauth2.config.converter.ProviderUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service

@Service
class CustomOidcUserService: AbstractOAuth2UserService(), OAuth2UserService<OidcUserRequest, OidcUser> {

  override fun loadUser(userRequest: OidcUserRequest): OidcUser {
    val clientRegistration = userRequest.clientRegistration
    val userService = OidcUserService()
    val oidcUser = userService.loadUser(userRequest)

    val providerUserRequest = ProviderUserRequest(clientRegistration, oidcUser)
    val providerUser = providerUser(providerUserRequest)
    super.register(providerUser, userRequest)

    return oidcUser
  }
}