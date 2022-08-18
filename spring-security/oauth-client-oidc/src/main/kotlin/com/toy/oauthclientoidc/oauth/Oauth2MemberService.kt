package com.toy.oauthclientoidc.oauth

import com.toy.oauthclientoidc.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class Oauth2UserService(
  private val userRepository: UserRepository
) {

}