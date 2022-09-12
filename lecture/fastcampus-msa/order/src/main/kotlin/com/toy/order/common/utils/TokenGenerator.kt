package com.toy.order.common.utils

import org.apache.commons.lang3.RandomStringUtils

object TokenGenerator {
  const val TOKEN_LENGTH = 20

  fun randomCharacter(length: Int) = RandomStringUtils.randomAlphanumeric(length)

  fun randomCharacterWithPrefix(prefix: String) = prefix + RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH - prefix.length)
}