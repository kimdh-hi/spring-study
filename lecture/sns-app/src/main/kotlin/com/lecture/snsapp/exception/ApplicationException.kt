package com.lecture.snsapp.exception

class ApplicationException(
  val errorCode: ErrorCode,
  override val message: String? = null
): RuntimeException(message) {
  fun getErrorMessage() = String.format("%s. %s", errorCode.message, message)
}