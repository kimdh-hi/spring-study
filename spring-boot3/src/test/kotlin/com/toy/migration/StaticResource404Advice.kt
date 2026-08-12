package com.toy.migration

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class CatchAllAdvice {

  @ExceptionHandler(Exception::class)
  fun handle(e: Exception): ResponseEntity<String> =
    ResponseEntity.internalServerError().body(e.message)
}

@RestControllerAdvice
class NoResourceAwareAdvice {

  @ExceptionHandler(NoResourceFoundException::class)
  fun handleNoResource(e: NoResourceFoundException): ResponseEntity<String> =
    ResponseEntity.status(e.statusCode).body(e.message)

  @ExceptionHandler(Exception::class)
  fun handle(e: Exception): ResponseEntity<String> =
    ResponseEntity.internalServerError().body(e.message)
}
