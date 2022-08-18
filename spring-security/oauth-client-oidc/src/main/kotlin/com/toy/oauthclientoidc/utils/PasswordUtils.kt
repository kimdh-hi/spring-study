package com.toy.oauthclientoidc.utils

import org.springframework.security.crypto.factory.PasswordEncoderFactories

object PasswordUtils {

  val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

  fun encode(charSequence: CharSequence) = passwordEncoder.encode(charSequence)
  fun matches(charSequence: CharSequence, encodedPssword: String) = passwordEncoder.matches(charSequence, encodedPssword)

}