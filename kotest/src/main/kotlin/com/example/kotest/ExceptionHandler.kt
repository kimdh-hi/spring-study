package com.example.kotest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Exception

@RestControllerAdvice
class ExceptionHandler {

  @ExceptionHandler
  fun exceptionHandler(exception: Exception): ResponseEntity<String> {
    return ResponseEntity.internalServerError().body(exception.message ?: "???")
  }
}