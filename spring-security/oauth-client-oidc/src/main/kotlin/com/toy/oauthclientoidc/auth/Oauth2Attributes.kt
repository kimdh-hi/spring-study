package com.toy.oauthclientoidc.auth

import com.toy.oauthclientoidc.common.Idp
import com.toy.oauthclientoidc.domain.User
import com.toy.oauthclientoidc.utils.PasswordUtils
import org.apache.commons.lang3.RandomStringUtils
import kotlin.reflect.KFunction2

data class Oauth2Attributes(
  val email: String,
  val nickname: String,
  val attributes: Map<String, Any>
) {

  companion object {
    val ATTRIBUTES_BY_PROVIDER: HashMap<Idp, KFunction2<Oauth2Attributes, Map<String, Any>, Oauth2Attributes>> =
      hashMapOf(
        Idp.GOOGLE to Oauth2Attributes::ofGoogleAttributes
    )
  }

  fun toUser(): User {
    val password = PasswordUtils.encode(RandomStringUtils.randomAlphanumeric(10))
    return User.newUser(name = nickname, username = email, encodedPassword = password)
  }

  fun ofGoogleAttributes(attributes: Map<String, Any>): Oauth2Attributes {
    return Oauth2Attributes(
      email = attributes["email"].toString(),
      nickname = attributes["name"].toString(),
      attributes = attributes
    )
  }
}