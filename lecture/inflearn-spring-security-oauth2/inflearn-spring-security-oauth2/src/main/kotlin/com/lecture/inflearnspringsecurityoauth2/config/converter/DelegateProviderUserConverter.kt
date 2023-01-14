package com.lecture.inflearnspringsecurityoauth2.config.converter

import com.lecture.inflearnspringsecurityoauth2.domain.User
import com.lecture.inflearnspringsecurityoauth2.model.ProviderUser
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class DelegateProviderUserConverter: ProviderUserConverter<ProviderUserRequest, ProviderUser> {

  var converters: List<ProviderUserConverter<ProviderUserRequest, out ProviderUser>> = listOf(
    OAuth2GoogleProviderUserConverter(),
    OAuth2NaverProviderUserConverter()
  )

  override fun convert(providerUserRequest: ProviderUserRequest): ProviderUser? {
    converters.forEach { converter ->
      val providerUser = converter.convert(providerUserRequest)
      if(providerUser != null)
        return providerUser
    }

    throw RuntimeException("not supported providerUserRequest...")
  }
}

data class ProviderUserRequest(
  val clientRegistration: ClientRegistration? = null,
  val oAuth2User: OAuth2User? = null,
  val user: User? = null
)