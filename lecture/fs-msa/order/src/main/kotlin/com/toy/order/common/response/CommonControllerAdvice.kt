package com.toy.order.common.response

import com.toy.order.common.exception.BaseException
import com.toy.order.common.exception.ErrorCodes
import com.toy.order.common.interceptor.CommonHttpRequestInterceptor
import org.apache.catalina.connector.ClientAbortException
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.core.NestedExceptionUtils
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class CommonControllerAdvice {

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
     val CRITICAL_ERROR_CODES = listOf<String>()
  }

  /**
   * status: 200
   * log: warn or error
   * CommonResponseResult: Fail
   *
   * 의도한 예외, 예상한 예외에 대한 처리
   */
  @ExceptionHandler(BaseException::class)
  fun handleBaseException(e: BaseException): ResponseEntity<CommonResponse<Any>> {
    val eventId = MDC.get(CommonHttpRequestInterceptor.HEADER_REQUEST_UUID_KEY)
    if(CRITICAL_ERROR_CODES.contains(e.errorCode)) {
      log.error("[BaseException] eventId={}, message={}", eventId, e.message)
    } else {
      log.warn("[BaseException] eventId={}, message={}", eventId, e.message)
    }
    return ResponseEntity.ok(CommonResponse.fail(message = e.message, errorCode = e.errorCode))
  }

  /**
   * status: 400
   * log: error
   * CommonResponseResult: Fail
   *
   * 입력값 검증에 대한 예외
   */
  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<CommonResponse<Any>> {
    val eventId = MDC.get(CommonHttpRequestInterceptor.HEADER_REQUEST_UUID_KEY)
    log.warn("[BaseException] eventId = {}, message = {}", eventId,
      NestedExceptionUtils.getMostSpecificCause(e).message
    )
    val bindingResult = e.bindingResult
    val fieldError = bindingResult.fieldError
    val commonResponse = if (fieldError != null) {
      val message = "Request Error ${fieldError.field}=${fieldError.rejectedValue} (${fieldError.defaultMessage})"
      CommonResponse.fail(message, ErrorCodes.INVALID_PARAMETER)
    } else {
      CommonResponse.fail(ErrorCodes.INVALID_PARAMETER, ErrorCodes.INVALID_PARAMETER)
    }
    return ResponseEntity.badRequest().body(commonResponse)
  }

  /**
   * status: 200
   * log: warn
   *
   * 의도한 예외도 예상한 예외도 아니지만 무시해도 무방한 예외에 대한 처리
   */
  @ExceptionHandler(value = [ClientAbortException::class])
  fun handleSkipNotExpectedException(e: Exception): ResponseEntity<CommonResponse<Any>> {
    val eventId = MDC.get(CommonHttpRequestInterceptor.HEADER_REQUEST_UUID_KEY)
    log.warn("[BaseException] eventId={}, message={}", eventId, e.message)
    return ResponseEntity.ok(CommonResponse.fail(errorCode = ErrorCodes.UNKNOWN_SYSTEM_ERROR))
  }
}