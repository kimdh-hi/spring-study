package com.toy.springexception.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class ExceptionHandler {

  @ExceptionHandler
  fun noResourceFoundException(e: NoResourceFoundException): ResponseEntity<ErrorResponse> =
    ErrorResponses.apiNotFound(e.message)

  @ExceptionHandler
  fun exceptionHandler(e: Exception): ResponseEntity<ErrorResponse> =
    ErrorResponses.from(e)
}
