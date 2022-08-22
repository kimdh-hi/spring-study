package com.toy.oauthbasic.oauth2

import com.toy.oauthbasic.domain.User

data class OAuth2Attributes(
  val email: String,
  val name: String,
  val attributes: Map<String, Any>
) {

  companion object {
    fun of(registrationId: String, attributes: Map<String, Any>): OAuth2Attributes {
      return when(Idp.valueOf(registrationId.uppercase())) {
        Idp.GOOGLE -> ofGoogle(attributes)
        Idp.NAVER -> ofNaver(attributes)
      }
    }

    private fun ofGoogle(attributes: Map<String, Any>): OAuth2Attributes {
      return OAuth2Attributes(
        email = attributes["email"] as String,
        name = attributes["name"] as String,
        attributes = attributes
      )
    }

    private fun ofNaver(attributes: Map<String, Any>): OAuth2Attributes {
      val response: Map<String, Any> = attributes["response"] as Map<String, Object>

      return OAuth2Attributes(
        email = response["email"] as String,
        name = response["name"] as String,
        attributes = attributes
      )
    }
  }

  fun toUser(): User {
    return User(
      username = this.email,
      name = this.name,
      password = ""
    )
  }
}