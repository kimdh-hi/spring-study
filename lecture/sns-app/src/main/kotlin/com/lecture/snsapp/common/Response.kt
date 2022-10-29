package com.lecture.snsapp.common

class Response<T>(
  val resultCode: String,
  val result: T? = null
) {
  companion object {
    fun error(errorCode: String) = Response<Unit>(resultCode = errorCode)

    fun <T> success(
      resultCode: String = "SUCCESS",
      result: T? = null
    ): Response<T> = Response(resultCode = resultCode, result = result)
  }

  fun toHttpResponse(): String {
    return result?.let {
      """{
          "resultCode": $resultCode,
          "result": $result
      }""".trimIndent()
    } ?: run {
      """{
          "resultCode": $resultCode,
          "result": ${null}              
      }""".trimIndent()
    }
  }
}