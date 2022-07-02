package com.toy.jpabasic.common

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

  private val log = LoggerFactory.getLogger(javaClass)

  @ExceptionHandler(Exception::class)
  fun handleException(ex: Exception) {
    log.error(ex.message, ex)
  }
}