package com.toy.springexception.exception

import jakarta.servlet.ServletException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object ErrorResponses {

  private val logger = LoggerFactory.getLogger(ErrorResponses::class.java)

  fun from(e: Throwable): ResponseEntity<ErrorResponse> =
    when (val cause = unwrap(e)) {
      is KnownException -> {
        logger.warn("known error. errorCode=${cause.errorCodes.code}, message=${cause.message}")
        ResponseEntity(ErrorResponse.of(cause.errorCodes, cause.message), HttpStatus.BAD_REQUEST)
      }

      else -> {
        logger.error("unknown error.", cause)
        ResponseEntity(ErrorResponse.of(ErrorCodes.UNKNOWN, cause.message), HttpStatus.INTERNAL_SERVER_ERROR)
      }
    }

  fun apiNotFound(message: String?): ResponseEntity<ErrorResponse> {
    logger.warn("errorCode: ${ErrorCodes.API_NOT_FOUND.code}, errorMessage: $message")
    return ResponseEntity(ErrorResponse.of(ErrorCodes.API_NOT_FOUND, "api not found."), HttpStatus.BAD_REQUEST)
  }

  private fun unwrap(e: Throwable): Throwable {
    var current = e
    while (current is ServletException && current.cause != null) {
      current = current.cause!!
    }
    return current
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
