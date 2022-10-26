package com.toy.springmvc.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.io.Serial
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(ConstraintViolationException::class)
  fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ErrorResponseVO> {
    val responseVO = ErrorResponseVO.error("9002", "Request body is not valid..")
    return ResponseEntity(responseVO, HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(ParameterException::class)
  fun handleParameterException(ex: ParameterException): ResponseEntity<ErrorResponseVO> {
    val responseVO = ErrorResponseVO.error("9002", "Request body is not valid..")
    return ResponseEntity(responseVO, HttpStatus.BAD_REQUEST)
  }
}

class ParameterException(): RuntimeException()

data class ErrorResponseVO(
  var errorCode: String,
  var message: Any?
) {

  companion object {
    @Serial
    private const val serialVersionUID: Long = -6603405402325753950L

    fun error(errorCode: String, message: Any?): ErrorResponseVO {
      return ErrorResponseVO(errorCode, message ?: "Unknown exception..")
    }
  }

}
