package com.example.ex.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestControllerAdvice {

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
}