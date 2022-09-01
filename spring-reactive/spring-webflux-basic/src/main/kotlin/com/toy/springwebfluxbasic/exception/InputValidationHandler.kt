package com.toy.springwebfluxbasic.exception

import com.toy.springwebfluxbasic.dto.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class InputValidationHandler {

  @ExceptionHandler(InputValidationException::class)
  fun handleInputValidationException(e: InputValidationException): ResponseEntity<ErrorResponse> {
    val response = ErrorResponse(
      errorCode = InputValidationException.ERROR_CODE,
       message = InputValidationException.MESSAGE)
    return ResponseEntity.badRequest().body(response)
  }
}