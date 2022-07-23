package com.toy.webfluxr2dbcpostgres.common

import com.rsupport.xr.api.base.ErrorResponseVO
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class GlobalControllerAdvice {

  private val log = LoggerFactory.getLogger(javaClass)

  @ExceptionHandler(WebExchangeBindException::class)
  fun handleWebExchangeBindException(e: WebExchangeBindException): ResponseEntity<ErrorResponseVO> {
    return parameterException(ParameterException(e.bindingResult))
  }

  @ExceptionHandler(ParameterException::class)
  fun parameterException(e: ParameterException): ResponseEntity<ErrorResponseVO> {
    log.error("error!", e)

    val responseVO = ErrorResponseVO.error(e.getErrorCode(), e.errors)
    return ResponseEntity(responseVO, HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(HttpMessageNotReadableException::class)
  fun parameterException(e: HttpMessageNotReadableException, request: WebRequest): ResponseEntity<ErrorResponseVO> {
    log.error("error!", e)

    val responseVO = ErrorResponseVO.error(ErrorCodes.PARAMETER_NOT_VALID, "Request body is not valid..")
    return ResponseEntity(responseVO, HttpStatus.BAD_REQUEST)
  }


  @ExceptionHandler(BaseException::class)
  fun baseException(e: BaseException, request: WebRequest): ResponseEntity<ErrorResponseVO> {
    return handleException(HttpStatus.INTERNAL_SERVER_ERROR, request, e)
  }

  @ExceptionHandler(Exception::class)
  fun exception(e: Exception, request: WebRequest): ResponseEntity<ErrorResponseVO> {
    return handleException(HttpStatus.INTERNAL_SERVER_ERROR, request, e)
  }

  fun handleException(status: HttpStatus, request: WebRequest, e: Exception): ResponseEntity<ErrorResponseVO> {
    log.error("error!", e)

    val errorCode = if (e is BaseException) {
      e.getErrorCode()
    } else {
      ErrorCodes.UNKNOWN
    }

    val responseVO = ErrorResponseVO.error(errorCode, e.message ?: "UNKNOWN ERROR...")
    return ResponseEntity(responseVO, status)
  }
}