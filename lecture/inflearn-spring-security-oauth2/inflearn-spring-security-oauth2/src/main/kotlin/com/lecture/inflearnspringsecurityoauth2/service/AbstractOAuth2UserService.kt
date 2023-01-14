package com.lecture.inflearnspringsecurityoauth2.service

import com.lecture.inflearnspringsecurityoauth2.config.converter.ProviderUserConverter
import com.lecture.inflearnspringsecurityoauth2.config.converter.ProviderUserRequest
import com.lecture.inflearnspringsecurityoauth2.model.*
import com.lecture.inflearnspringsecurityoauth2.model.social.GoogleUser
import com.lecture.inflearnspringsecurityoauth2.model.social.KeycloakUser
import com.lecture.inflearnspringsecurityoauth2.model.social.NaverUser
import com.lecture.inflearnspringsecurityoauth2.model.OAuth2ProviderUser
import com.lecture.inflearnspringsecurityoauth2.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
abstract class AbstractOAuth2UserService {

  @Autowired
  lateinit var userService: UserService

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var providerUserConverter: ProviderUserConverter<ProviderUserRequest, ProviderUser>

  fun register(providerUser: ProviderUser?, userRequest: OAuth2UserRequest) {
    providerUser ?: throw RuntimeException("providerUser not exists...")

    userRepository.findByUsername(providerUser.getUsername()) ?: run {
      val registrationId = userRequest.clientRegistration.registrationId
      userService.save(registrationId, providerUser)
    }

  }

  protected fun providerUser(providerUserRequest: ProviderUserRequest): ProviderUser? {
    return providerUserConverter.convert(providerUserRequest)
  }

  protected fun register(providerUser: OAuth2ProviderUser, oAuth2UserRequest: OAuth2UserRequest) {

  }
}