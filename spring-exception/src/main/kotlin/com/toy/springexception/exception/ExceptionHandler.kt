package com.toy.springexception.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class ExceptionHandler {

  private val logger = LoggerFactory.getLogger(Exception::class.java)

  @ExceptionHandler
  fun noResourceFoundException(e: NoResourceFoundException): ResponseEntity<ErrorResponse> {
    logger.warn("errorCode: ${ErrorCodes.API_NOT_FOUND.code}, errorMessage: ${e.message}")
    val responseVO = ErrorResponse.of(ErrorCodes.API_NOT_FOUND, "api not found.")
    return ResponseEntity(responseVO, HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler
  fun exceptionHandler(e: Exception): ResponseEntity<ErrorResponse> {
    val (response, httpStatusCode) = when (e) {
      is KnownException -> {
        logger.warn("known error. errorCode=${e.errorCodes.code}, message=${e.message}")
        Pair(ErrorResponse.of(e.errorCodes, e.message), HttpStatus.BAD_REQUEST)
      }

      else -> {
        logger.error("unknown error.", e)
        Pair(ErrorResponse.of(ErrorCodes.UNKNOWN, e.message), HttpStatus.INTERNAL_SERVER_ERROR)
      }
    }

    return ResponseEntity(response, httpStatusCode)
  }
}

data class ErrorResponse(
  var errorCode: String,
  var message: Any? = null,
) {
  companion object {
    fun of(errorCodes: ErrorCodes, message: String?) = ErrorResponse(
      errorCode = errorCodes.code,
      message = message
    )
  }
}
