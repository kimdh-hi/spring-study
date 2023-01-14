package com.lecture.inflearnspringsecurityoauth2.utils

import com.lecture.inflearnspringsecurityoauth2.model.social.Attributes
import org.springframework.security.oauth2.core.user.OAuth2User

object OAuth2Utils {

  fun getMainAttributes(oAuth2User: OAuth2User): Attributes {
    return Attributes(
      mainAttributes = oAuth2User.attributes
    )
  }

  fun getSubAttributes(oAuth2User: OAuth2User, subAttributeKey: String): Attributes {
    val subAttributes = oAuth2User.attributes[subAttributeKey] as Map<String, Any>
    return Attributes(
      subAttributes = subAttributes
    )
  }

  fun getOtherAttributes(oAuth2User: OAuth2User, subAttributeKey: String, otherAttributeKey: String): Attributes {
    val subAttributes = oAuth2User.attributes[subAttributeKey] as Map<String, Any>
    val otherAttributes = subAttributes[otherAttributeKey] as Map<String, Any>
    return Attributes(
      subAttributes = subAttributes,
      otherAttributes = otherAttributes
    )
  }
}