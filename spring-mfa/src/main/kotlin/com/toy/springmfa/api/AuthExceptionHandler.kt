package com.toy.springmfa.api

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthExceptionHandler {

  @ExceptionHandler(AuthenticationException::class)
  fun unauthorized(exception: AuthenticationException): ProblemDetail =
    ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.message)
}
