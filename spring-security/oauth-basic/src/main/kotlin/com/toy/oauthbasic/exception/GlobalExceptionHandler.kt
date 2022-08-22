package com.toy.oauthbasic.exception

import com.toy.oauthbasic.vo.ErrorResponseVO
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(Exception::class)
  fun exception(e: Exception, request: WebRequest): ResponseEntity<ErrorResponseVO> {
    val responseVO = ErrorResponseVO("9999", e.message)
    return ResponseEntity(responseVO, HttpStatus.INTERNAL_SERVER_ERROR)
  }

}