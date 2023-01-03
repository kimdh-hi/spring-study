package com.toy.springmvc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.io.Serial
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler(
  private val objectMapper: ObjectMapper
) {

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

  // requestBody @Valid
  @ExceptionHandler(value = [MethodArgumentNotValidException::class])
  fun methodArgumentNotValidExceptionHandler(e: MethodArgumentNotValidException): ResponseEntity<String> {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectMapper.writeValueAsString(e.bindingResult))
  }

  // modelAttribute @Valid
  @ExceptionHandler(value = [BindException::class])
  fun bindExceptionHandler(e: BindException): ResponseEntity<String> {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectMapper.writeValueAsString(e.bindingResult))
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
