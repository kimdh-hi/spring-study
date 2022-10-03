package com.lecture.snsapp.exception

import com.lecture.snsapp.common.Response
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalControllerAdvice {
  private val log = LoggerFactory.getLogger(javaClass)

  @ExceptionHandler(ApplicationException::class)
  fun handleApplicationException(ex: ApplicationException): ResponseEntity<Response<Unit>> {
    log.error("error occurs {}", ex.message)
    return ResponseEntity.badRequest()
      .body(Response.error(ex.errorCode.name))
  }

  @ExceptionHandler(RuntimeException::class)
  fun handleRuntimeException(ex: RuntimeException): ResponseEntity<Response<Unit>> {
    log.error("error occurs {}", ex.message)
    return ResponseEntity.internalServerError()
      .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name))
  }
}