package com.toy.oauthbasic.service

import com.toy.oauthbasic.domain.User
import com.toy.oauthbasic.domain.UserConnect
import com.toy.oauthbasic.oauth2.Idp
import com.toy.oauthbasic.oauth2.OAuth2Attributes
import com.toy.oauthbasic.repository.UserConnectRepository
import com.toy.oauthbasic.repository.UserRepository
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserConnectService(
  private val userRepository: UserRepository,
  private val userConnectRepository: UserConnectRepository
) {

  fun getUserByOidc(userRequest: OidcUserRequest, oidcUser: OidcUser): User {
    val oAuth2Attributes = OAuth2Attributes.of(userRequest.clientRegistration.registrationId, oidcUser.attributes)
    return getUser(oAuth2Attributes, userRequest.clientRegistration.registrationId)
  }

  fun getUserByOAuth2(userRequest: OAuth2UserRequest, oAuth2User: OAuth2User): User {
    val oAuth2Attributes = OAuth2Attributes.of(userRequest.clientRegistration.registrationId, oAuth2User.attributes)
    return getUser(oAuth2Attributes, userRequest.clientRegistration.registrationId)
  }

  private fun getUser(oAuth2Attributes: OAuth2Attributes, registrationId: String): User {
    return userRepository.findByUsername(oAuth2Attributes.email) ?: run {
      val user = User(
        name = oAuth2Attributes.name,
        username = oAuth2Attributes.email,
        password = ""
      )
      val savedUser = userRepository.save(user)
      val userConnect = UserConnect.of(user = savedUser, idp = Idp.valueOf(registrationId.uppercase()))
      userConnectRepository.save(userConnect)
      savedUser
    }
  }
}