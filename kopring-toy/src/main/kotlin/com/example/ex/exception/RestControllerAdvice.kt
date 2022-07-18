package com.example.ex.exception

import com.example.ex.common.ErrorResponseVO
import org.slf4j.LoggerFactory
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class RestControllerAdvice(
  val errorAttributes: ErrorAttributes
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @ExceptionHandler(RuntimeException::class)
  fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ErrorResponseVO> {
    val responseVO = ErrorResponseVO("9000", ex.message!!)
    return ResponseEntity(responseVO, HttpStatus.INTERNAL_SERVER_ERROR)
  }

  @ExceptionHandler(ParameterException::class)
  fun handleParameterException(ex: ParameterException): ResponseEntity<ErrorResponseVO> {
    val responseVO = ErrorResponseVO("9001", ex.message!!)
    return ResponseEntity(responseVO, HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(BaseException::class)
  fun handleBaseException(ex: BaseException, request: WebRequest): ResponseEntity<ErrorResponseVO> {
    return handleException(HttpStatus.INTERNAL_SERVER_ERROR, request, ex)
  }

  @ExceptionHandler(Exception::class)
  fun handleException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponseVO> {
    return handleException(HttpStatus.INTERNAL_SERVER_ERROR, request, ex)
  }

  fun handleException(status: HttpStatus, request: WebRequest, e: Exception): ResponseEntity<ErrorResponseVO> {
    log.error("error!", e)

    val errorCode = if (e is BaseException) {
      e.getErrorCode()
    } else {
      ErrorCodes.UNKNOWN
    }

    val errorAttributeMap =
      errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE))

    val responseVO = ErrorResponseVO.error(errorCode, errorAttributeMap["message"] ?: e.message)
    return ResponseEntity(responseVO, status)
  }
}