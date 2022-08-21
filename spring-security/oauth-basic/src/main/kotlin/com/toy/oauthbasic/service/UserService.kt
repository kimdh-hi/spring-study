package com.toy.oauthbasic.service

import com.toy.oauthbasic.domain.User
import com.toy.oauthbasic.domain.UserOAuth2Login
import com.toy.oauthbasic.oauth2.Idp
import com.toy.oauthbasic.repository.UserOAuth2LoginRepository
import com.toy.oauthbasic.repository.UserRepository
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository,
  private val userOAuth2LoginRepository: UserOAuth2LoginRepository
) {

  @Transactional
  fun getUserByOAuth2(request: OidcUserRequest, oidcUser: OidcUser): User {
    return userRepository.findByUsername(oidcUser.email) ?: run {
      // 최초 oauth2 로그인을 시도한 경우
      val user = User(
        name = oidcUser.name,
        username = oidcUser.email,
        password = ""
      )
      val savedUser = userRepository.save(user)
      val userOAuth2Login = UserOAuth2Login.of(user = savedUser, idp = Idp.GOOGLE)
      userOAuth2LoginRepository.save(userOAuth2Login)
      savedUser
    }
  }

  @Transactional
  fun getUserByOAuth2(request: OAuth2UserRequest, oAuth2User: OAuth2User): User {
    return userRepository.findByUsername(oAuth2User.attributes["username"] as String) ?: run {
      // 최초 oauth2 로그인을 시도한 경우
      val user = User(
        name = oAuth2User.name,
        username = oAuth2User.attributes["username"] as String,
        password = ""
      )
      val savedUser = userRepository.save(user)
      val userOAuth2Login = UserOAuth2Login.of(user = savedUser, idp = Idp.GOOGLE)
      userOAuth2LoginRepository.save(userOAuth2Login)
      savedUser
    }
  }
}