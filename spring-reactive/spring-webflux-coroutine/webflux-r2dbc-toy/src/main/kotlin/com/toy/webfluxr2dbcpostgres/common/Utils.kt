package com.toy.webfluxr2dbcpostgres.common

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.crypto.factory.PasswordEncoderFactories

object MapperUtils {
  val objectMapper = ObjectMapper()

  fun toJson(obj: Any) = objectMapper.writeValueAsString(obj)
}

object PasswordUtils {
  val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
  fun encode(rawPassword: CharSequence): String = passwordEncoder.encode(rawPassword)
  fun matches(rawPassword: CharSequence, encodedPassword: String) = passwordEncoder.matches(rawPassword, encodedPassword)
}