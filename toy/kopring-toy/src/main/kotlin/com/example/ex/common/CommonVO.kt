package com.example.ex.common

import java.io.Serial

data class ErrorResponseVO(
  var errorCode: String,
  var message: Any?
) {

  companion object {
    @Serial
    private const val serialVersionUID: Long = -6603405402325753950L

    fun error(errorCode: String, message: Any?): ErrorResponseVO {
      return ErrorResponseVO(errorCode, message ?: "Unknown exception..")
    }
  }

}
