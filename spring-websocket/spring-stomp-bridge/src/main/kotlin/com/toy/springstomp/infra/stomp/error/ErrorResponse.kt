package com.toy.springstomp.infra.stomp.error

data class ErrorResponse(
  val code: String,
  val message: String,
) {
  companion object {
    fun of(errorCode: ErrorCode): ErrorResponse = ErrorResponse(errorCode.name, errorCode.message)
  }
}
