package com.lecture.issueservice.exception

import com.auth0.jwt.exceptions.TokenExpiredException
import mu.KotlinLogging
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
  private val log = KotlinLogging.logger{}

  @ExceptionHandler(ServerException::class)
  fun handleServerException(ex: ServerException): ErrorResponse {
    log.error { ex.message }
    return ErrorResponse(ex.code, ex.message)
  }

  @ExceptionHandler(TokenExpiredException::class)
  fun handleTokenExpiredException(ex: TokenExpiredException): ErrorResponse {
    log.error { ex.message }
    return ErrorResponse(code = 401, message = "token expired...")
  }

  @ExceptionHandler(Exception::class)
  fun handleException(ex: Exception): ErrorResponse {
    log.error { ex.message }
    return ErrorResponse(code = 500, message = ex.message ?: "unexpected server error...")
  }
}