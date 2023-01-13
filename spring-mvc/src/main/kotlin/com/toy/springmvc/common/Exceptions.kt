package com.toy.springmvc.common

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
