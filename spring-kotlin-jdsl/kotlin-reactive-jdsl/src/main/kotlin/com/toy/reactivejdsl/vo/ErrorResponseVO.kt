package com.toy.reactivejdsl.vo

import com.toy.reactivejdsl.common.BaseVO
import java.io.Serial

data class ErrorResponseVO(
  var errorCode: String,
  var message: Any?
) {

  companion object {
    @Serial
    private const val serialVersionUID: Long = -6603405402325753950L

    fun error(errorCode: String, message: Any?): ErrorResponseVO {
      return com.toy.reactivejdsl.vo.ErrorResponseVO(errorCode, message ?: "Unknown exception..")
    }
  }
}