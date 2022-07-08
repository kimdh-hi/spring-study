package com.toy.webfluxr2dbcpostgres.common

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException::class)
  fun handleRuntimeException(ex: RuntimeException)
    = ResponseEntity.internalServerError().body(ErrorResponse("9000", ex.message!!))
}

data class ErrorResponse(
  val errorCode: String,
  val message: String
)