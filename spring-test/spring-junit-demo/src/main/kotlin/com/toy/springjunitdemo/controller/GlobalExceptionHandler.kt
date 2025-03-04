package com.toy.springjunitdemo.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException::class)
  fun exceptionHandler(ex: RuntimeException): ResponseEntity<Unit> = ResponseEntity.internalServerError().build()
}