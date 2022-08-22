package com.toy.oauthbasic.vo

import java.io.Serial

data class ErrorResponseVO(
  var errorCode: String,
  val message: String? = null
) {

  companion object {
    @Serial
    private const val serialVersionUID: Long = -6603405402325753950L

    fun error(errorCode: String, message: String?): ErrorResponseVO {
      return ErrorResponseVO(errorCode, message ?: "Unknown exception..")
    }
  }

}