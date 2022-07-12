package com.toy.reactivejdsl.common

import com.toy.reactivejdsl.vo.ErrorVO
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalControllerAdvice {

  private val log = LoggerFactory.getLogger(javaClass)

  @ExceptionHandler(RuntimeException::class)
  fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ErrorVO> {
    log.error(ex.message!!)
    val vo = ErrorVO(errorCode = "9000", message = "error...")
    return ResponseEntity(vo, HttpStatus.INTERNAL_SERVER_ERROR)
  }
}