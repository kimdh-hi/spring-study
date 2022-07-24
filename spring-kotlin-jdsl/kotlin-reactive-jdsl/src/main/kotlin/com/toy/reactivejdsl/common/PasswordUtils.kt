package com.toy.reactivejdsl.common

import org.springframework.security.crypto.factory.PasswordEncoderFactories

object PasswordUtils {
  val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
  fun encode(rawPassword: CharSequence): String = passwordEncoder.encode(rawPassword)
  fun matches(rawPassword: CharSequence, encodedPassword: String) = passwordEncoder.matches(rawPassword, encodedPassword)
}