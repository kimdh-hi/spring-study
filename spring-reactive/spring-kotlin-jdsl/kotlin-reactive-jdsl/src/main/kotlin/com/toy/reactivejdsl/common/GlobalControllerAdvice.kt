package com.rsupport.xr.api.base

import com.toy.reactivejdsl.common.BaseException
import com.toy.reactivejdsl.common.ErrorCodes
import com.toy.reactivejdsl.common.ParameterException
import com.toy.reactivejdsl.vo.ErrorResponseVO
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException

@RestControllerAdvice
class GlobalExceptionHandler {

  private val log = LoggerFactory.getLogger(javaClass)

  @ExceptionHandler(WebExchangeBindException::class)
  fun handleWebExchangeBindException(e: WebExchangeBindException): ResponseEntity<ErrorResponseVO> {
    return parameterException(ParameterException(e.bindingResult))
  }

  private fun parameterException(e: ParameterException): ResponseEntity<ErrorResponseVO> {
    log.error("error!", e)

    val responseVO = ErrorResponseVO.error(e.getErrorCode(), e.errors)
    return ResponseEntity(responseVO, HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(HttpMessageNotReadableException::class)
  fun parameterException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponseVO> {
    log.error("error!", e)

    val responseVO = ErrorResponseVO.error(ErrorCodes.PARAMETER_NOT_VALID, "Request body is not valid..")
    return ResponseEntity(responseVO, HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
  fun accessDeniedException(e: org.springframework.security.access.AccessDeniedException): ResponseEntity<ErrorResponseVO> {
    log.error("error!", e)

    val responseVO = ErrorResponseVO.error(ErrorCodes.UNAUTHORIZED_ACCESS, "Access is denied")
    return ResponseEntity(responseVO, HttpStatus.FORBIDDEN)
  }

  @ExceptionHandler(BaseException::class)
  fun baseException(e: BaseException): ResponseEntity<ErrorResponseVO> {
    return handleException(HttpStatus.INTERNAL_SERVER_ERROR, e)
  }

  @ExceptionHandler(Exception::class)
  fun exception(e: Exception): ResponseEntity<ErrorResponseVO> {
    return handleException(HttpStatus.INTERNAL_SERVER_ERROR, e)
  }

  //todo, webRequest 대체 (ServerRequest 동작X)
  fun handleException(status: HttpStatus, e: Exception): ResponseEntity<ErrorResponseVO> {
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