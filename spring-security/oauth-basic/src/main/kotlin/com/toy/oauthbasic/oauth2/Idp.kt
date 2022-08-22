package com.toy.oauthbasic.oauth2

import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames

enum class Idp {

  GOOGLE {
    override fun getBuilder(registrationId: String): ClientRegistration.Builder {
      val builder = getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
      builder.scope("openid", "profile", "email")
      builder.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
      builder.tokenUri("https://www.googleapis.com/oauth2/v4/token")
      builder.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
      builder.issuerUri("https://accounts.google.com")
      builder.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
      builder.userNameAttributeName(IdTokenClaimNames.SUB)
      builder.clientName("Google")
      return builder
    }
  },
  NAVER {
    override fun getBuilder(registrationId: String): ClientRegistration.Builder {
      val builder = getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
      builder.scope("name", "email", "profile_image")
      builder.authorizationUri("https://nid.naver.com/oauth2.0/authorize")
      builder.tokenUri("https://nid.naver.com/oauth2.0/token")
      builder.userInfoUri("https://openapi.naver.com/v1/nid/me")
      builder.userNameAttributeName("response")
      builder.clientName("Naver")
      return builder
    }
  };

  protected fun getBuilder(registrationId: String, method: ClientAuthenticationMethod): ClientRegistration.Builder {
    val builder = ClientRegistration.withRegistrationId(registrationId)
    builder.clientAuthenticationMethod(method)
    builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
    builder.redirectUri(DEFAULT_REDIRECT_URL)
    return builder
  }

  abstract fun getBuilder(registrationId: String): ClientRegistration.Builder?

  companion object {
    private const val DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}"
    fun caseInsensitiveValueOf(name: String): Idp {
      return Idp.valueOf(name.uppercase())
    }
  }
}