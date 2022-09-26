package com.lecture.userservice.utils

import at.favre.lib.crypto.bcrypt.BCrypt

object BCryptUtils {

  fun encode(password: String): String = BCrypt.withDefaults().hashToString(12, password.toCharArray())

  fun verify(plainPassword: String, encodedPassword: String): Boolean =
    BCrypt.verifyer()
      .verify(plainPassword.toCharArray(), encodedPassword)
      .verified
}