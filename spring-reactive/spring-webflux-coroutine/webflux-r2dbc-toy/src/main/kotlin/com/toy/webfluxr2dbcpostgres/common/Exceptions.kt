package com.toy.webfluxr2dbcpostgres.common

import org.springframework.security.core.AuthenticationException
import org.springframework.validation.BindingResult
import java.io.Serial

open class BaseException(
  private val errorCode: String? = ErrorCodes.UNKNOWN,
  message: String? = null,
  cause: Throwable? = null
) : RuntimeException(message, cause) {

  fun getErrorCode() = errorCode!!

  companion object {
    @Serial
    private const val serialVersionUID: Long = 3416992995011541346L
  }
}

class ParameterException(bindingResult: BindingResult) : BaseException(ErrorCodes.PARAMETER_NOT_VALID) {

  var errors: MutableMap<String, MutableList<String>> = mutableMapOf()

  init {
    bindingResult.fieldErrors.forEach { error ->
      error.defaultMessage?.let { errors.getOrPut(error.field, ::mutableListOf).add(it) }
    }
  }

  companion object {
    @Serial
    private const val serialVersionUID: Long = -430281793895310306L
  }
}

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
