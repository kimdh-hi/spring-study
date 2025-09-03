package com.study.resilience4j.controller

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

  private val log = LoggerFactory.getLogger(ExceptionHandler::class.java)

  @ExceptionHandler
  fun handle(ex: CallNotPermittedException): ResponseEntity<ErrorResponse> {
    log.warn("CallNotPermittedException ex={}", ex.message)
    return ResponseEntity(ErrorResponse(1111, "CallNotPermittedException"), HttpStatus.INTERNAL_SERVER_ERROR)
  }

  @ExceptionHandler
  fun handle(ex: Exception): ResponseEntity<ErrorResponse> {
    return ResponseEntity(ErrorResponse(9999, "unknown error."), HttpStatus.INTERNAL_SERVER_ERROR)
  }
}


data class ErrorResponse(val code: Int, val message: String)
