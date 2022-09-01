package com.toy.springwebfluxbasic.exception

class InputValidationException: RuntimeException(MESSAGE) {

  companion object {
    const val MESSAGE = "allowed range is 10 to 20"
    const val ERROR_CODE = "9001"
  }
}