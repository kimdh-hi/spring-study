package com.toy.oauthbasic.oauth2

import com.toy.oauthbasic.repository.UserOAuth2LoginRepository
import com.toy.oauthbasic.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OAuth2UserService(
  private val userRepository: UserRepository,
  private val userOAuth2LoginRepository: UserOAuth2LoginRepository
) {

  fun get(username: String, registrationId: String) {

  }
}