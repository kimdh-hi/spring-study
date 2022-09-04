package com.toy.springwebfluxbasic.exception

class InputValidationException(
  message: String = MESSAGE,
  val errorCode: String = ERROR_CODE
): RuntimeException(message) {

  companion object {
    const val MESSAGE = "allowed range is 10 to 20"
    const val ERROR_CODE = "9001"
  }

  override fun toString(): String {
    return "InputValidationException(errorCode='$errorCode', message='$message')"
  }
}