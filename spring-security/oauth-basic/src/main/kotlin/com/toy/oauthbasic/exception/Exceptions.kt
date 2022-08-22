package com.toy.oauthbasic.exception

import org.springframework.security.core.AuthenticationException
import java.io.Serial

class JwtAuthenticationException(
  message: String,
  cause: Throwable? = null
) : AuthenticationException(message, cause) {

  companion object {
    @Serial
    private const val serialVersionUID: Long = 1786701870442613802L
  }
}

class JwtExpiredException(
  message: String,
  cause: Throwable? = null
) : AuthenticationException(message, cause) {

  companion object {
    @Serial
    private const val serialVersionUID: Long = 1030955050984723990L
  }
}
