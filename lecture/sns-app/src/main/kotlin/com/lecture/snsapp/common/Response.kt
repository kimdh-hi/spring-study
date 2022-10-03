package com.lecture.snsapp.common

class Response<T>(
  val resultCode: String,
  val result: T? = null
) {
  companion object {
    fun error(errorCode: String) = Response<Unit>(resultCode = errorCode)

    fun <T> success(
      resultCode: String = "SUCCESS",
      result: T
    ): Response<T> = Response(resultCode = resultCode, result = result)
  }
}