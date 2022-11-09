package com.toy.order.common.response

/*
정상응답, 예외응답 모두 아래 공통의 응답을 갖게 한다.
 */
data class CommonResponse<T>(
  val result: Result,
  val data: T? = null,
  val message: String? = null,
  val errorCode: String? = null,
) {
  companion object {
    fun <T> success(data: T, message: String? = null) =
      CommonResponse(
        result = Result.SUCCESS,
        data = data,
        message = message
      )

    fun fail(message: String? = null, errorCode: String) =
      CommonResponse<Any>(
        result = Result.FAIL,
        message = message,
        errorCode = errorCode
      )
  }

  enum class Result {
    SUCCESS, FAIL
  }
}