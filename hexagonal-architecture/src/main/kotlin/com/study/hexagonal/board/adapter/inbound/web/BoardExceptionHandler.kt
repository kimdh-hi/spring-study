package com.study.hexagonal.board.adapter.inbound.web

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BoardExceptionHandler {

  @ExceptionHandler(IllegalArgumentException::class)
  fun badRequest(e: IllegalArgumentException): ProblemDetail =
    ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message ?: "잘못된 요청")

  @ExceptionHandler(NoSuchElementException::class)
  fun notFound(e: NoSuchElementException): ProblemDetail =
    ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message ?: "대상을 찾을 수 없음")
}
